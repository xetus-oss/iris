package com.xetus.oss.iris;

public class UserPrincipalConverter {
  private FreeIPAConfig config;
  public UserPrincipalConverter(FreeIPAConfig config) {
    this.config = config;
  }

  public String getUser(String user, String realm) {
    if (realm == null && !user.matches("@")) {
      if (config.getRealm() == null) {
        throw new IllegalArgumentException("Realm is required to open a "
        + "connection to the FreeIPA instance");
      }
      realm = config.getRealm();
    }

    if (!user.matches("@")) {
      user = user + "@" + realm;
    }

    return user;
  }
}
