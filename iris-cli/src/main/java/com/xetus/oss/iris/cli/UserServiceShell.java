package com.xetus.oss.iris.cli;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import com.xetus.oss.iris.FreeIPAClient;

@ShellComponent
public class UserServiceShell {

  private ShellFormatter formatter = new ShellFormatter();
  private AuthenticatedFreeIPAClientService clientService;
  
  @Autowired
  public UserServiceShell(AuthenticatedFreeIPAClientService clientService) {
    this.clientService = clientService;
  }
  
  public FreeIPAClient getClient() {
    return this.clientService.getClient();
  }
  
  @ShellMethodAvailability
  public Availability shellCommandsAvailable() {
    return this.clientService != null ? 
      Availability.available() : 
        Availability.unavailable("Authentication required");
  }
  
  @ShellMethod(
      group = "query", 
      value = "Get a listing of users from the FreeIPA server"
  )
  public String listUsers(@ShellOption String... uids)  {
    try {
      return formatter.format(
          getClient().userFind(Arrays.asList(uids))
      );
    } catch(Exception e) {
      e.printStackTrace();
      return "Failed to show all users";
    }
  }
  
}
