package com.xetus.oss.iris.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xetus.oss.iris.FreeIPAClient;
import com.xetus.oss.iris.FreeIPAConfig;

@Component
public class AuthenticatedFreeIPAClientService {

  private FreeIPAConfig config;
  private FreeIPAClient client;
  
  @Autowired
  AuthenticatedFreeIPAClientService(FreeIPAConfig config) {
    this.config = config;
  }
  
  public FreeIPAConfig getConfig() {
    return this.config;
  }
  
  public void setClient(FreeIPAClient client) {
    this.client = client;
  }
  
  public FreeIPAClient getClient() {
    return this.client;
  }
  
  public boolean authenticated() {
    return this.client != null;
  }
}
