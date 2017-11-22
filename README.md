# Overview

This project defines a set of java libraries to facilitate authenticated
consumption of the FreeIPA RPC API from Java. The project supports 
LDAP and Kerberos authentication with the FreeIPA API.

## Install

At this time the project is not published to any artifact repositories.

However, you can build and publish it's artifacts to your local Maven 
repository:

```
./gradlew publishToMavenLocal
```

## Usage

The project is split into distinct libraries, depending on how you might
want to use it:

* [iris-auth](./iris-auth): the base library that handles construction
of authenticated [JsonRpcClient](https://github.com/briandilley/jsonrpc4j)
instances;
* [iris-rpc-client-base](./iris-rpc-client-base): a library that provides
a client with abstracted, type-safe RPC commands that can be used by
consumers to interact with the FreeIPA RPC isntance (after adding some
model bindings);
* [iris-rpc-client-default-bidnings](./iris-rpc-client-default-bindings): 
A set of default model bindings for the rpc-client-base project; and
* [iris-cli](./iris-cli): a command line utility to test out othe project
against a FreeIPA instance.