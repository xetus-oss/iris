# Overview

A Java library for consuming FreeIPA's JSON-RPC API that handles establishing an RPC session using either Kerberos authentication or a username and password. Please see the comments in the FreeIPAConfig, FreeIPAAuthenticationManager and FreeIPAClient for more details.

# Example Usage

### Authenticating Using User Session
Authenticating using FReeIPA user credentials is relatively straightforward:

```
FreeIPAConfig config = new FreeIPAConfig();
config.setHostname("freeipa.example.com");
config.setRealm("EXAMPLE.COM");
FreeIPAAuthenticationManager mgr = new FreeIPAAuthenticationManager(config);

try {
	FreeIPAClient client = mgr.getSessionClient("user", "pass");
} catch(Exception e) {
	// handle invalid username/password, expired password, etc...
}

...
```

### Authenticating Using Kerberos Credentials
Kerberos authentication uses JAAS with the Krb5LoginModule, the configuration of which can be somewhat confusing. While it's possible the JAAS mechanism and Kerberos authentication module is only available on certain distributions of Java (i.e. Oracle), this has yet to be verified. That said, the best thing to do is to read through [Oracle's documentation on HTTP authentication using JAAS](http://docs.oracle.com/javase/7/docs/technotes/guides/net/http-auth.html), specifically the section titled "Http Negotiate (SPNEGO)".

To allow the Iris library to authenticate using Kerberos:

1. Create a JAAS configuration file that looks something like the following, changing the principal/keytab properties as applicable. You'll want to specify the absolute file path to the FreeIPAConfig object passed to the FreeIPAAuthenticationManager:
 	
 	_jaas.conf_
 	
	```	
	com.sun.security.jgss.krb5.initiate {
		com.sun.security.auth.module.Krb5LoginModule required
	   	doNotPrompt=true
	   	useKeyTab=true
	   	keyTab="/etc/iris/keytab"
	   	storeKey=true
	   	useTicketCache=true
	   	principal="user@EXAMPLE.COM"; 
	};
	```
	
2. The JVM must be able to locate a Kerberos configuration file (krb5.conf). While the Kerberos login module should search in the host system's default locations, if for any reason it cannot locate the default Kerberos  you can define your own and specify it's location using the FreeIPAConfig.krb5ConfigPath property. Please refer to the relevant Kerberos documentation for more information on how to setup the krb5.conf file. An example minimal configuration might look something like:

	_krb5.conf_
	
	```	
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
	
Once the host environment is setup with the above configurations, you can use them with the Iris library with something along the following lines:

```
FreeIPAConfig config = new FreeIPAConfig();

// Standard configuration
config.setHostname("freeipa.example.com");
config.setRealm("EXAMPLE.COM");

// Kerberos Configuration


config.setJaasConfigPath("/path/to/jaas.conf");

// Only necessary if the JVM doesn't have a 
// default krb5.conf file it can pick up 
config.setKrb5ConfigPath("/path/to/krb5.conf");


FreeIPAAuthenticationManager mgr = new FreeIPAAuthenticationManager(config);
try {
	FreeIPAClient client = mgr.getKerberosClient();
} catch(Exception e) {
	// handle invalid username/password, expired password, etc...
}

...
```

### Using the FreeIPAClient
As of the time of this writing, almost no abstraction is applied to generating the JSON-RPC requests or processing the JSON-RPC responses received, although hopefully that will be abstracted in the future. Current use of the FreeIPAClient looks something like:

```
// get a FreeIPAClient instance using either 
// of the authentication methods described above
RPCResult response = client.userFind(null, new HashMap<String, String>() {{
	"uid": "user" 
}});

List<Map> results = (List<Map>) response.getResult();
if (results == null || results.size() < 1) {
	// nothing found
}

// NOTE: this ugliness will hopefully be abstracted in a later version
Map userMap = results.get(0);
List emails = ((List) userMap.get("mail"));
String email = emails.get(0);

...
```