package com.xetus.oss.iris.http;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

/**
 * A {@link Configuration} object explicitly dedicated for 
 * programmatically configuring the Krb5LoginModule. This avoids the
 * need for consumers to create and specify a JAAS configuration file,
 * and for that JAAS configuration to be JVM-wide.
 */
public class KerberosClientLoginConfig extends Configuration {

  private final String keytabPath;
  private final String principal;
  private final Map<String, Object> loginOptions;

  public KerberosClientLoginConfig(String keytabPath, 
                                   String principal, 
                                   Map<String, Object> loginOptions) {
    super();
    this.keytabPath = keytabPath;
    this.principal = principal;
    this.loginOptions = loginOptions;
  }

  @Override
  public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
    Map<String, Object> options = new HashMap<>();
    options.put("doNotPrompt", "true");
    options.put("useKeyTab", "true");
    options.put("keyTab", this.keytabPath);
    options.put("storeKey", "true");
    options.put("useTicketCache", "true");
    options.put("principal", this.principal);
    options.put("isInitiator", "true");
    
    if (loginOptions != null) {
      options.putAll(loginOptions);
    }

    return new AppConfigurationEntry[] {
      new AppConfigurationEntry(
        "com.sun.security.auth.module.Krb5LoginModule",
        AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, 
        options
      )
    };
  }
}
