# Overview

A Java library for consuming FreeIPA's JSON-RPC API that handles establishing an RPC session using either Kerberos authentication or a username and password. Please see the comments in the FreeIPAConfig, FreeIPAAuthenticationManager and FreeIPAClient for more details.

## Usage :: Command Line Console

For quick and dirty testing purposes, a simple command line console utility is bundled with the Iris project (TODO: factor into a separate sub-project that generates a separate artifact). This is mostly for demonstrative purposes as the FreeIPA JSON-RPC API is really just an abstraction of the FreeIPA command line tools anyhow.

To use the command line console, download the source and run the following:

```
#
# Install the CLI's distributable to a local folder
#
./gradlew installDist

#
# Start the CLI console
#
./build/install/iris/bin/iris
```

Once the console has started, you'll need to do a few things to start interacting with the FreeIPA instance.

#### 1. Configure the Console's JVM Keystore
*Note: this is likely only required if the FreeIPA instance to which you're attempting to connect uses a self-signed certificate that isn't in your default JVM*

For convenience, the parent console of the utility exposes a function for re-configuring the JVM's keystore. If this is something you need, start the command line console and run the following, replacing {path_to_your_keystore} with the relative path to the keystore you want the console's JVM to use:

```
RPCClient> set-java-keystore {path_to_your_keystore}
```

#### 2. Start a User Session

In order to interact with a FreeIPA instance, you must establish a session with the FreeIPA instance. At this time the command line console only supports the session client and not the Kerberos client. To establish a session:

```
RPCClient> authenticate {HOST_NAME} {USER_NAME} {PASSWORD} {KERBEROS_REALM}
```

If all goes as planned, you should see a sub-shell start up:

```
success
RPCClient/user@realm>
```

#### 3. Interacting with the FreeIPA Instance

You're now ready to interact with the FreeIPA instance using the Iris command line console. To see a list of supported RPC calls, run:

```
RPCClient/user@realm> ?list
```

##  Usage :: Library
The library exposes a configurable java-friendly abstraction of the FreeIPA JSON RPC API.

### Authenticated Session Types
FreeIPA exposes two authentication engines that can be used to establish the authenticated JSON RPC session: LDAP, and Kerberos. Both require some small amount of preparation to ensure the JVM can establish a secure authenticated session with the FreeIPA instance.

#### LDAP Session
Establishing an authenticated FreeIPAClient using LDAP credentials requires minimal preparation:

1. Ensure the keystore for the JVM running the Iris library trusts the FreeIPA instance's SSL certifiacte.
2. Create and configure a `FreeIPAConfig` instance as applicable and retrieve a `FreeIPAAuthenticationManager` instance:

		FreeIPAConfig config = new FreeIPAConfig();
		config.setHostname("freeipa.example.com");
		config.setRealm("EXAMPLE.COM");

3. Retrieve a `FreeIPAAuthenticationManager` instance and use it to establish an authenticated `FreeIPAClient` instance using the `FreeIPAAuthenticationManager#mgr.getSessionClient(username, password)` method:
		
		FreeIPAAuthenticationManager mgr = new FreeIPAAuthenticationManager(config);
		FreeIPAClient client;
		try {
			client = mgr.getSessionClient("user", "pass");
		} catch(Exception e) {
			// handle invalid username/password, expired password, etc...
		}
		// you should now be able to interact with the FreeIPA server
		// using the `client` instance

#### Kerberos Session
Establishing an authenticated FreeIPAClient using Kerberos authentication requires much more involved preparation. Kerberos authentication uses JAAS with the Krb5LoginModule; for more information, read through [Oracle's documentation on HTTP authentication using JAAS](http://docs.oracle.com/javase/7/docs/technotes/guides/net/http-auth.html), specifically the section titled "Http Negotiate (SPNEGO)".

To allow the Iris library to authenticate using Kerberos:

1. On the JVM's host system, create a JAAS configuration file similar to the following, replacing the `keyTab` and `principal` attributes with the path to a valid Kerberos keytab for the FreeIPA instance and the principal in the keytab to use for authentication with the Iris library:
 	
 	_jaas.conf_
 	
		com.sun.security.jgss.krb5.initiate {
			com.sun.security.auth.module.Krb5LoginModule required
		   	doNotPrompt=true
		   	useKeyTab=true
		   	keyTab="/etc/iris/keytab"
		   	storeKey=true
		   	useTicketCache=true
		   	principal="user@EXAMPLE.COM"; 
		};
	
	
2. On the JVM's host system, configure a Kerberos configuration file; the location of the configuration file can be overriden with the `FreeIPAConfig` if the configuration is not placed in any of the default paths for any reason. Please refer to the relevant Kerberos documentation for more information on how to setup the krb5.conf file. An example minimal configuration might look something like:

	_krb5.conf_
			
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
		
3. Create and configure a `FreeIPAConfig` instance as applicable:

		FreeIPAConfig config = new FreeIPAConfig();
		config.setHostname("freeipa.example.com");
		config.setRealm("EXAMPLE.COM");

4. Configure the `FreeIPAConfig` instance with any additional Kerberos-specific configurations that are required:
		
		// specify path to the jaas.conf file created in step (1)
		config.setJaasConfigPath("/path/to/jaas.conf");
		
		// specify path to the krb5.conf file created in step (2)
		// note: should only be necessary if the configuration file is 
		// not palced at a default path
		config.setKrb5ConfigPath("/path/to/krb5.conf"); 

5. Retrieve a `FreeIPAAuthenticationManager` instance and use it to establish an authenticated `FreeIPAClient` instance using the `FreeIPAAuthenticationManager#getKerberosClient()` method:
		
		FreeIPAAuthenticationManager mgr = new FreeIPAAuthenticationManager(config);
		FreeIPAClient client = null;
		try {
			client = mgr.getKerberosClient();
		} catch(Exception e) {
			// handle invalid username/password, expired password, etc...
		}
		// you should now be able to interact with the FreeIPA server
		// using the `client` instance


### Using the FreeIPAClient
Once an authenticated `FreeIPAClient` instance has been successfully retrieved, you can use it to interact with the FreeIPA instace:


	FreeIPAClient client
	// get a FreeIPAClient instance using either 
	// of the authentication sessions described above
	...
	
	RPCResult<List<User>> response = client.userFind(null, new HashMap<String, String>() {{
		"uid": "user" 
	}});
	
	List<User> results = response.getResult();
	if (results == null || results.size() < 1) {
		System.out.println("No users found")
	}

	User user = results.get(0);
	String dn = user.getDn();
	System.out.println("Found user with dn: " + dn);
	
#### Configuring The Model Type Factory
The `FreeIPAClient` uses a model type factory as a crude injection mechanism for the model classes to use when deserializing certain JSON RPC response result objects. This is included for two reasons:

1. to allow consuming applications to limit the model object to only those attributes that are required by the consuming library (per the Tolerant Reader pattern); and
2. to allow consuming applications to inject their own model classes to reflect customized LDAP schemas in the FreeIPA instance.

To customize the moddel type factory, simply register the class for your override instances with the `FreeIPAConfig` instsance's `typeFactory`:
	
	...
	FreeIPAConfig config = new FreeIPAConfig();
	config.getTypeFactory().registerUserClass(MyCustomUser.class);
	...
	
	FreeIPAAuthenticationManager mgr = new FreeIPAAuthenticationManager(config);
	FreeIPAClient client =	mgr.getSessionClient("user", "pass");
	RPCResponse<List<MyCustomUser>> response = (RPCResponse<List<MyCustomUser>>) client
		.userFind(null, new HashMap<String, String>());
	...
	
Please see the `com.xetus.iris.model.freeipa.account` model classes for example model classes and note that the JSON RPC parser library uses Jackson as it's serialization framework. An exceedingly minimal default model type factory implementation is automatically configured for the FreeIPAConfig instance using the model classes in the example package, although that is mostly for demonstrative purposes.