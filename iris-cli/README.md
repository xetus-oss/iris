# Overview

This command line application is intended to be used for testing out 
the Iris library and any requisite configurations (e.g. FreeIPA, etc...)

## Usage

To build the command line application:

```
./gradlew installDist
```

To run the application:

```
./iris-cli/build/install/iris-cli/bin/iris-cli
```

## Known Issues

For some reason, the built application ignores any trustore 
configurations. This makes using this library against a FreeIPA instance
with a self-signed cert impossible.