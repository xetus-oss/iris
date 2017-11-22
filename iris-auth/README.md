# Overview

This library provides the base interface for establishing an 
authenticated [JsonRpcClient](https://github.com/briandilley/jsonrpc4j)
against a FreeIPA RPC API. Both LDAP and Kerberos authentication are
supported.

## Usage

Each of the authentication scenarios require some configruation.

### Pre-Requisites

Each of the following sections assumes you have an open network path
from the `iris-auth` library consumer and the FreeIPA instance, and 
that the JVM running the library trusts the FreeIPA instance's SSL
certificate.

#### LDAP Authentication

Establishing an LDAP authentication session requires configuring at 
least the hostname and realm for the FreeIPA instance:

```java
/*
 * Create a configuration object with the FreeIPA hostname and the
 * realm for authentication
 */
FreeIPAConfig config = new FreeIPAConfig();
config.setHostname("freeipa.example.com");
config.setRealm("EXAMPLE.COM");

/*
 * Create a FreeIPAAuthenticationManager instance using your config
 * object and establish an authenication session using a valid 
 * principal and password
 */
FreeIPAAuthenticationManager mgr = new FreeIPAAuthenticationManager(config);
JsonRPCClient client;
try {
  client = mgr.getSessionClient("user_principal", "password");
} catch(Exception e) {
  // handle authentication / connection errors
  // ...
}
// Use your new authenticatied RPC client to do things

Map<String, String> params = new HashMap<>();
params.put("version", "2.114");
Map result = client.invoke(
    "user_find", 
    Arrays.asList(new ArrayList<String>(), params),
    Map.class
);
```

#### Kerberos Authentication

Establishing a Kerberos authentication session is a little more involved.
Due to the way Java handles Kerberos authenticaitons, you'll need to
supply a [krb5.conf](https://web.mit.edu/kerberos/krb5-1.12/doc/admin/conf_files/krb5_conf.html)
file to the authentication manager:

1. On the JVM's host system, create and configure a Kerberos 
configuration file:

  ```ini
  [libdefaults]
    default_realm = EXAMPLE.COM
     dns_lookup_realm = true
     dns_lookup_kdc = true
     ticket_lifetime = 24h
     forwardable = true
  
  [realms]
 	EXAMPLE.COM = {
      kdc = freeipa.example.com
      admin_server = freeipa.example.com
    }
  
  [domain_realm]
    .example.com = EXAMPLE.COM
    example.com = EXAMPLE.COM
  ```

  _Note: make sure to onfigure `forwardable=true` to avoid cryptic FreeIPA
  server errors!_

2. Configure the hostname, realm, and path to the `krb5.conf` file you
created above:


  ```java
  /*
   * Create a configuration object with the FreeIPA hostname, realm,
   * and path to the krb5.conf file
   */
  FreeIPAConfig config = new FreeIPAConfig();
  config.setHostname("freeipa.example.com");
  config.setRealm("EXAMPLE.COM");
  config.setKrb5ConfigPath("etc/iris/krb5.conf");
  
  /*
   * Create a FreeIPAAuthenticationManager instance using your config
   * object and establish an authenication KErberos session
   */
  FreeIPAAuthenticationManager mgr = new FreeIPAAuthenticationManager(config);
  JsonRPCClient client;
  try {
    client = mgr.getRPCKerberosClient();
  } catch(Exception e) {
    // handle invalid keytab and connetion exceptions, etc...
    // ...
  }
  // Use your new authenticatied RPC client to do things
  
  Map<String, String> params = new HashMap<>();
  params.put("version", "2.114");
  Map result = client.invoke(
      "user_find", 
      Arrays.asList(new ArrayList<String>(), params),
      Map.class
  );
  ```