package com.xetus.oss.iris.http;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

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
    options.put("useKeyTab", "true");
      options.put("keyTab", this.keytabPath);
      options.put("principal", this.principal);
      options.put("storeKey", "true");
      options.put("doNotPrompt", "true");
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
