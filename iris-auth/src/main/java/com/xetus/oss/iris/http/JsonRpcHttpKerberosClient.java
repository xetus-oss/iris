package com.xetus.oss.iris.http;

import java.lang.reflect.Type;
import java.net.URL;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.xetus.oss.iris.FreeIPAConfig;
import com.xetus.oss.iris.InvalidKeytabException;
import com.xetus.oss.iris.UserPrincipalConverter;

public class JsonRpcHttpKerberosClient extends JsonRpcHttpClient {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(JsonRpcHttpKerberosClient.class);

  
  private final FreeIPAConfig config;
  private final Map<String, Object> loginOptions;
  private LoginContext loginContext;
  private UserPrincipalConverter userConverter;

  public JsonRpcHttpKerberosClient(ObjectMapper mapper, 
                                   URL serviceUrl, 
                                   Map<String, String> headers,
                                   FreeIPAConfig config) {
    this(mapper, serviceUrl, headers, config, new HashMap<String, Object>());
  }

  public JsonRpcHttpKerberosClient(ObjectMapper mapper, 
                                   URL serviceUrl, 
                                   Map<String, String> headers,
                                   FreeIPAConfig config,
                                   Map<String, Object> options) {
    super(mapper, serviceUrl, headers);
    this.config = config;
    this.userConverter = new UserPrincipalConverter(config);
    this.loginOptions = options;
  }
  
  public String getPrincipal() {
    return userConverter.getUser(config.getPrincipal(), config.getRealm());
  }

  /**
   * Wraps the {@link JsonRpcHttpClient#invoke} call in a "do as" 
   * privileged execution using the configured Kerberos credentials.
   * {@inheritDoc}
   */
  @Override
  public Object invoke(String methodName,
                       Object argument, 
                       Type returnType,
                       Map<String, String> extraHeaders) 
                throws InvalidKeytabException,
                       Throwable {
     try {
       /*
        * While you can configure the Kerberos KDC and realm 
        * programmatically via system properties (java.security.krb5.kdc
        * and java.security.krb5.realm, respectively), you can't 
        * configure any other krb5.conf configurations (other than via
        * Krb5LoginModule). We happen to need to set `forwardable` to 
        * true.
        * 
        * @see https://pagure.io/freeipa/issue/4745
        * @see http://bugs.java.com/bugdatabase/view_bug.do?bug_id=4460829
        * 
        * Note that this is configured here, at the last minute, to 
        * ensure the desired state when we need it. 
        */
       System.setProperty("java.security.krb5.conf", config.getKrb5ConfigPath());
       getLoginContext().login();
       Subject serviceSubject = getLoginContext().getSubject();
       LOGGER.trace("subject: {}", serviceSubject);
       return Subject.doAs(serviceSubject, new PrivilegedAction<Object>() {
         @Override
         public Object run() {
             LOGGER.trace("Issuing RPC request: \n" +
                          "method: {}\n" +
                          "argument: {}\n" +
                          "headers: {}\n",
                          methodName, argument, extraHeaders);
             try {
               return JsonRpcHttpKerberosClient.this.doInvoke(
                 methodName, 
                 argument, 
                 returnType, 
                 extraHeaders
               );
             } catch(Throwable e) {
               throw new RuntimeException(
                 "Failed to invoke Kerberos request for method: " + 
                 methodName, e
               );
             }
         }
       });
 
     } catch (LoginException e) {
       throw new InvalidKeytabException("Error running rest call", e);
     }
  }

  private Object doInvoke(String methodName,
                          Object argument, 
                          Type returnType,
                          Map<String, String> extraHeaders) throws Throwable {
    return super.invoke(methodName, argument, returnType, extraHeaders);
  }
  
  private LoginContext getLoginContext() throws LoginException {
    if (this.loginContext == null) {
      KerberosClientLoginConfig loginConfig = new KerberosClientLoginConfig(
        config.getKeytabPath(),
        getPrincipal(),
        loginOptions
      );
      Set<Principal> princ = new HashSet<Principal>(1);
      princ.add(new KerberosPrincipal(getPrincipal()));
      Subject sub = new Subject(
        false,
        princ,
        new HashSet<Object>(),
        new HashSet<Object>()
      );
      this.loginContext = new LoginContext("", sub, null, loginConfig);
    }
    return this.loginContext;
  }
}
