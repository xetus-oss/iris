package com.xetus.iris

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import java.util.concurrent.TimeUnit

import org.apache.http.Header
import org.apache.http.HttpResponse
import org.apache.http.client.CookieStore
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.config.SocketConfig
import org.apache.http.conn.HttpClientConnectionManager
import org.apache.http.cookie.Cookie
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.message.BasicNameValuePair

import com.googlecode.jsonrpc4j.JsonRpcHttpClient
import com.xetus.iris.jackson.databind.ObjectMapperBuilder

/**
 * Manages authenticating against the FreeIPA server and establishing
 * a valid session for use with the target freeIPA server's JSON-RPC
 * API. This class handles authenticating against the two separate
 * session establishing mechanisms exposed by the FreeIPA portal, namely:<ol>
 * 
 *  <li>{@link #getKerberosClient()} authenticates using the JAAS 
 *  {@link com.sun.security.auth.module.Krb5LoginModule} configuration, and
 *  <li>{@link #getSessionClient(String, String, String)} authenticates
 *  using the passed user name and password (and optionally realm).
 *  
 * </ol>
 * 
 * <h5>1. Authenticating Using Kerberos</h5>
 * Kerberos authentication uses JAAS with the {@link 
 * com.sun.security.auth.module.Krb5LoginModule}. Because the documentation
 * for JAAS largely indicates configuration through system properties and
 * configuration files, in order for Karberos authentication to work consuming
 * projects must set the {@link FreeIPAConfig#jaasConfigPath} and, if the
 * authentication should use a krb5.conf file from a locaiton other than the
 * system default, the {@link FreeIPAConfig#krb5ConfigPath}. On successful
 * authentication, the {@link #getKerberosClient()} method will return an
 * {@link FreeIPAClient}
 * 
 * <h5>2. Authenticating Using Account Credentials</h5>
 * Authentication via account credentials should be as simple as passing in
 * the username and password to the {@link #getSessionClient(String, String, 
 * String)} method.
 * 
 * <h5>Notes</h5>
 * The {@link FreeIPAClient} returned in both of the above scenarios will
 * have the same access privileges as the accounts used to authenticate them.
 */
@Slf4j
@CompileStatic
class FreeIPAAuthenticationManager {

  FreeIPAConfig config

  PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS)

  FreeIPAAuthenticationManager(FreeIPAConfig c) {
    this.config = c
    this.cm.setDefaultMaxPerRoute(10)
    this.cm.setMaxTotal(30)
  }

  private String getUser(String user, String realm = null) {
    if (!realm && !user.contains('@')) {
      if (!config.realm) {
        throw new IllegalArgumentException("Realm is required to open a "
        + "connection to the FreeIPA instance")
      }
      realm = config.realm
    }

    if (!user.contains('@')) {
      user = "$user@$realm"
    }

    return user
  }

  private HttpPost getPost(URI target) {
    HttpPost post = new HttpPost(target)
    post.addHeader("Accept", ContentType.APPLICATION_XML.mimeType)
    post.addHeader("Content-Type",
        ContentType.APPLICATION_FORM_URLENCODED.mimeType)
    return post
  }

  private CloseableHttpResponse execute(HttpPost post, CookieStore cookieStore) {
    CloseableHttpClient client = HttpClientBuilder
          .create()
          .useSystemProperties()
          .setConnectionManager(cm)
          .setDefaultCookieStore(cookieStore)
          .setDefaultSocketConfig(
             SocketConfig.custom()
                         .setSoTimeout(30*1000)
                         .setTcpNoDelay(true)
                         .build())
          .build()
    
    HttpResponse response = client.execute(post)
    log.debug("Post response:\n"
        + "code: ${response.statusLine.statusCode}\n"
        + "headers: ${response.getAllHeaders()}\n"
        + "content: ${response.getEntity()?.getContent()?.readLines()}\n"
        + "-----------\n\n")
    
    return response
  }

  URL getIpaUrl(String path = "") {
    new URL("https://${config.hostname}$path")
  }
  
  /**
   * @return {@link FreeIPAClient} that will authenticate against the
   * FreeIPA server using the Kerberos JAAS configuration specified
   * in the {@link FreeIPAConfig} object.
   */
  FreeIPAClient getKerberosClient() {
    config.applyKerberosProperties()
    return new FreeIPAClient(
      new JsonRpcHttpClient(
        ObjectMapperBuilder.getObjectMapper(),
        getIpaUrl("/ipa/json"),
        [
          "referer": getIpaUrl('/ipa').toString()
        ]
      ),
      config.getTypeFactory()
    )
  }

  /**
   * @return {@link FreeIPAClient} configured with the session ID and
   * the FreeIPA server's JSON-RPC endpoint. Note that a half-hearted
   * check will be made to ensure a session has been retrieved before
   * creating and returning a new client. 
   */
  FreeIPAClient getSessionClient(String user, String pass, String realm = null) {
    return new FreeIPAClient(
      new JsonRpcHttpClient(
        ObjectMapperBuilder.getObjectMapper(),
        getIpaUrl("/ipa/session/json"),
        [
          "Cookie": "ipa_session=" + connect(user, pass, realm),
          "referer": getIpaUrl('/ipa').toString()
        ]
      ),
      config.getTypeFactory()
    )
  }

  /**
   * Attempts to establish a connection using the passed username, password
   * and realm. If the realm is not passed, the username is expected to
   * contain the realm (e.g. user@REALM.COM). Returns the session Cookie if 
   * successfully authenticated.
   * 
   * @param user
   * @param pass
   * 
   * @throws PasswordExpiredException if the user's password has expired and
   * needs to be reset
   * @throws InvalidPasswordException if the supplied password is invalid for
   * the supplied user and realm
   * @throws InvalidUserOrRealmException if the supplied username and/or realm
   * is invalid for the host
   * @throws RuntimeException if any other error response is encountered from
   * FreeIPA
   */
  String connect(String user, String pass, String realm = null)
      throws PasswordExpiredException{

    user = getUser(user, realm)
    URI target = getIpaUrl("/ipa/session/login_password").toURI()
    log.trace "Connecting for user: $user at target: $target"

    HttpPost post = getPost(target)
    post.setEntity(new UrlEncodedFormEntity([
      new BasicNameValuePair("user", user),
      new BasicNameValuePair("password", pass)
    ]))

    CookieStore cookieStore = new BasicCookieStore()
    CloseableHttpResponse response = execute(post, cookieStore)
    try {
      if (response.getStatusLine().statusCode != 200) {
        if (response.getStatusLine().statusCode == 401) {
  
          Header[] reasons = response.getHeaders("X-IPA-Rejection-Reason")
  
          if (reasons.size() > 0) {
            if (reasons[0].value == "password-expired") {
              throw new PasswordExpiredException()
            }
  
            if (reasons[0].value == "invalid-password") {
              throw new InvalidPasswordException()
            }
  
            if (reasons[0].value == "denied") {
              throw new InvalidUserOrRealmException()
            }
  
          }
        }
        
        throw new RuntimeException("Encountered unexpected response from "
          + "FreeIPA; details:\n\n"
          + "code: ${response.statusLine.statusCode}\n"
          + "headers: ${response.getAllHeaders()}\n"
          + "content: ${response.getEntity()?.getContent()?.readLines()}\n"
          + "-----------\n\n")
      }
    } finally {
      response.close();
    }
    Cookie sessionCookie = cookieStore.getCookies().find {
      it.name == "ipa_session"  
    }
    
    return sessionCookie?.value
  }

  /**
   * Resets the user's password. Note that this need only be used in cases
   * where the user is unable to establish a session because their password
   * has expired. This includes scenarios where their password has been
   * reset by an administrator and FreeIPA immediately expires their 
   * password to force a reset.
   * 
   * @param user
   * @param oldPass
   * @param newPass
   * @param otp
   * @param realm
   * 
   * @throws PasswordExpiredException if the user's password expired
   * @throws InvalidPasswordException if an invalid oldPass was supplied
   * @throws PasswordPolicyViolationException if the newPass did not meet
   *  the password policy restrictions imposed on the server
   * @throws RuntimeException if the session timed out or an unexpected
   * error occured server-side
   * 
   */
  FreeIPAClient resetPassword(String user, String oldPass, String newPass,
                              String otp = null, String realm = null) {

    // for some reason here FreeIPA does not expect to have the realm passed
    user = user.replace(/\@.*/, "")
    URI target = getIpaUrl("/ipa/session/change_password").toURI()
    HttpPost post = getPost(target)

    List parameters = [
      new BasicNameValuePair("user", user),
      new BasicNameValuePair("old_password", oldPass),
      new BasicNameValuePair("new_password", newPass)
    ]

    if (otp != null) {
      parameters.add(new BasicNameValuePair("otp", otp))
    }

    post.setEntity(new UrlEncodedFormEntity(parameters))

    CloseableHttpResponse response = execute(post, new BasicCookieStore())
    try {
      if (response.getStatusLine().statusCode != 200) {
        if (response.getStatusLine().statusCode == 401) {
  
          Header[] reasons = response.getHeaders("X-IPA-Rejection-Reason")
  
          if (reasons.size() > 0 && reasons[0].value == "password-expired") {
            throw new PasswordExpiredException()
          }
        }
  
        throw new RuntimeException("Encountered unexpected response from "
          + "FreeIPA; details:\n\n"
          + "code: ${response.statusLine.statusCode}\n"
          + "headers: ${response.getAllHeaders()}\n"
          + "content: ${response.getEntity()?.getContent()?.readLines()}\n"
          + "-----------\n\n")
      }
    } finally {
      response.close();
    }

    Header[] reasons = response.getHeaders("X-IPA-Pwchange-Result")
    reasons.each { Header it ->
      if (it.value == "invalid-password") {
        throw new InvalidPasswordException("Invalid password")
      } else
      if (it.value == "policy-error") {
        Header[] policyErrors = response
          .getHeaders("X-IPA-Pwchange-Policy-Error")
        throw new PasswordPolicyViolationException(policyErrors[0].value)
      }
      if (it.value == "error") {
        throw new RuntimeException("Password change failed")
      }
    }

    return this.getSessionClient(user, newPass, realm)
  }

}
