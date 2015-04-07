package com.xetus.iris

import com.sun.security.auth.login.ConfigFile

import groovy.transform.CompileStatic

@CompileStatic
class FreeIPAConfig {
  
  static final String JAAS_CONFIG_PROPERTY = "java.security.auth.login.config"
  static final String KRB5_CONFIG_PROPERTY = "java.security.krb5.conf"
  static final String USE_SUBJECT_CREDS_ONLY_PROPERTY = 
    "javax.security.auth.useSubjectCredsOnly"
  
  String hostname
  String realm
    
  /**
   * Required to connect to the FreeIPA server using kerberos credentials
   * if the kerberos configruation file is not in a default location that 
   * will be recognized by the JRE.
   */
  String krb5ConfigPath
  
  /**
   * Required to connect to the FreeIPA server using kerberos credentials.
   * The configuration file should use the following format:
   * 
   * <pre>
   * com.sun.security.jgss.krb5.initiate {
   *  com.sun.security.auth.module.Krb5LoginModule required
   *    doNotPrompt=true
   *    useKeyTab=true
   *    keyTab="/etc/iris/keytab"
   *    storeKey=true
   *    useTicketCache=true
   *    principal="host/sd.example.com@EXAMPLE.COM"; 
   * };
   * </pre>
   * 
   * The configuration name <em>must</em> be <code>
   * com.sun.security.jgss.krb5.initiate</code> and must be for the 
   * Krb5LoginModule. See 
   * http://docs.oracle.com/javase/7/docs/technotes/guides/net/http-auth.html
   * in the section titled "Http Negotiate (SPNEGO)" for information on
   * properly configuring the jaas client.
   */
  String jaasConfigPath
  
  /**
   * This must be called before allowing consumers to issue any queries
   * against the FreeIPA server as this will configure the javax.security
   * authentication modules to initiate the HTTP negotiation protocol
   * with the FreeIPA server using the Kerberos configurations. The affected
   * system properties are:<ul>
   * 
   *  <li>java.security.auth.login.config (if {@link #customJaasConfigFilePath}
   *  is specified
   *  <li>java.security.krb5.conf (if {@link #krb5ConfigFilePath} is
   *  specified
   *  <li>javax.security.auth.useSubjectCredsOnly is set to false
   *  
   * </ul>
   */
  void applyKerberosProperties() {
    if (jaasConfigPath) {
      System.setProperty(JAAS_CONFIG_PROPERTY, jaasConfigPath)
    }
    
    if (krb5ConfigPath) {
      System.setProperty(KRB5_CONFIG_PROPERTY, krb5ConfigPath)
    }
    
    System.setProperty(USE_SUBJECT_CREDS_ONLY_PROPERTY,"false");
  }
}
