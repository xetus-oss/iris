package com.xetus.iris.cli

import groovy.json.JsonOutput;
import groovy.transform.CompileStatic
import asg.cliche.Command
import asg.cliche.InputConverter
import asg.cliche.Param
import asg.cliche.Shell
import asg.cliche.ShellDependent
import asg.cliche.ShellFactory

import com.xetus.iris.FreeIPAAuthenticationManager
import com.xetus.iris.FreeIPAClient
import com.xetus.iris.FreeIPAConfig
import com.xetus.iris.InvalidPasswordException;
import com.xetus.iris.InvalidUserOrRealmException;
import com.xetus.iris.PasswordExpiredException;

@CompileStatic
class Cli implements ShellDependent {
  
  public static void main(String[] args) throws IOException {
    ShellFactory.createConsoleShell("RPCClient", "RPCClient Start Loop", new Cli())
                .commandLoop()
  }
  
  public static final InputConverter[] CLI_INPUT_CONVERTERS = [
    new MapInputConverter()
  ]
  
  private Shell shell
  
  public void cliSetShell(Shell shell) {
    this.shell = shell
  }
  
  @Command(description = "set the Java SSL keystore")
  public void setJavaKeystore(
      @Param(name="keystore", description="path to keystore") String path) {
    System.setProperty("javax.net.ssl.trustStore", path)
  }
  
  @Command(description = "authenticate with the specified FreeIPA instance")
  public String authenticate(
      @Param(name="host", description="FreeIPA instance domain") String host,
      @Param(name="user", description="Authentication user") String user,
      @Param(name="password", description="Authentication password") String password,
      @Param(name="realm", description="Realm") String realm) {
    
    FreeIPAConfig config = new FreeIPAConfig()
    config.hostname = host
    FreeIPAClient client
    try {
      client = new FreeIPAAuthenticationManager(config).getSessionClient(user, password, realm)
    } catch (InvalidPasswordException e) {
      return "Failed to authenticate: Invalid password"
    } catch (InvalidUserOrRealmException e) {
      return "Failed to authenticate: Invalid username or realm"
    } catch (PasswordExpiredException e) {
      return "Failed to authenticate: Password has expired"
    } catch (Exception e) {
      return "Failed to authenticate: $e";
    }
    
    ShellFactory.createSubshell(
        "$user@$realm", 
        shell, 
        "Successfully started session", 
        new AuthenticatedShell(client)
    )
    .commandLoop()
  }
}
