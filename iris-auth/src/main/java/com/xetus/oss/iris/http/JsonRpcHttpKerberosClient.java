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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.xetus.oss.iris.InvalidKeytabException;

public class JsonRpcHttpKerberosClient extends JsonRpcHttpClient {

  private final String keytabPath;
  private final String principal;
  private final Map<String, Object> loginOptions;
  private LoginContext loginContext;

  public JsonRpcHttpKerberosClient(ObjectMapper mapper, 
                                   URL serviceUrl, 
                                   Map<String, String> headers,
                                   String keytabPath,
                                   String principal) {
    super(mapper, serviceUrl, headers);
    this.keytabPath = keytabPath;
    this.principal = principal;
    this.loginOptions = new HashMap<String, Object>();
  }

  public JsonRpcHttpKerberosClient(ObjectMapper mapper, 
                                   URL serviceUrl, 
                                   Map<String, String> headers,
                                   String keytabPath,
                                   String principal,
                                   Map<String, Object> options) {
    super(mapper, serviceUrl, headers);
    this.keytabPath = keytabPath;
    this.principal = principal;
    this.loginOptions = options;
  }

  /**
   * Wraps the {@link JsonRpcHttpClient#invoke} call in a "do as" 
   * privileged execution using the configured Kerberos credentials.
   * 
   * ... hope this works.
   *  
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
       getLoginContext().login();
       Subject serviceSubject = getLoginContext().getSubject();
       return Subject.doAs(serviceSubject, new PrivilegedAction<Object>() {
         @Override
         public Object run() {
           try {
             return JsonRpcHttpKerberosClient.this.doInvoke(
               methodName, 
               argument, 
               returnType, 
               extraHeaders
             );
           } catch(Throwable e) {
             throw new RuntimeException(e);
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
        keytabPath,
        principal,
        loginOptions
      );
      Set<Principal> princ = new HashSet<Principal>(1);
      princ.add(new KerberosPrincipal(principal));
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
