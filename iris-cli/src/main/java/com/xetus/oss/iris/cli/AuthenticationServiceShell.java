package com.xetus.oss.iris.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.xetus.oss.iris.FreeIPAAuthenticationManager;
import com.xetus.oss.iris.FreeIPAClient;
import com.xetus.oss.iris.FreeIPAConfig;
import com.xetus.oss.iris.model.DefaultFreeIPAResponseModelTypeFactory;

@ShellComponent
public class AuthenticationServiceShell {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceShell.class);
  
  private AuthenticatedFreeIPAClientService clientService;
  private FreeIPAAuthenticationManager authMgr;
  
  @Autowired
  public AuthenticationServiceShell(AuthenticatedFreeIPAClientService clientService) {
    this.clientService = clientService;
    this.authMgr = new FreeIPAAuthenticationManager(getConfig());
  }
  
  private FreeIPAConfig getConfig() {
    return this.clientService.getConfig();
  }
  
  @ShellMethod(group = "auth", value = "Authenticate with FreeIPA credentials")
  public String authenticate(@ShellOption String user,
                             @ShellOption String password) {
    try {
      JsonRpcHttpClient rpcClient = this.authMgr.getSessionClient(
          user, 
          password, 
          getConfig().getRealm()
      );
      this.clientService.setClient(createClient(rpcClient));
    } catch(Exception e) {
      e.printStackTrace();
      return "Failed to authenticate!\n\n";
    }
    return "Successfully authenticated as " + user + "@" + getConfig().getHostname();
  }
  
  @ShellMethod(group = "auth", value = "Authenticate with Kerberos credentials")
  public String authenticateKerberos() {
    try {
      this.clientService.setClient(
          createClient(this.authMgr.getRPCKerberosClient())
      );
    } catch(Exception e) {
      e.printStackTrace();
      return "Failed to authenticate!\n\n";
    }
    return "Successfully authenticated as " + 
            getConfig().getPrincipal() + "@" + getConfig().getHostname();
  }
  
  private FreeIPAClient createClient(JsonRpcHttpClient httpClient) {
    return new FreeIPAClient(
        httpClient, 
        new DefaultFreeIPAResponseModelTypeFactory()
    );
  }
}
