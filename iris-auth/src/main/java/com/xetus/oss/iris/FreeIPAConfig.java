package com.xetus.oss.iris;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

class FreeIPAConfig {
  
  private String hostname;
  private String realm;
  private String keytabPath;
  private String principal;
  private ObjectMapper rpcObjectMapper;

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public String getRealm() {
    return realm;
  }

  public void setRealm(String realm) {
    this.realm = realm;
  }

  
  /**
   * @return the path to the Kerberos keytab that should be used to 
   * establish an authenticated Kerberos RPC session with the FreeIPA 
   * server's KDC.
   */
  public String getKeytabPath() {
    return keytabPath;
  }

  public void setKeytabPath(String keytabPath) {
    this.keytabPath = keytabPath;
  }

  /**
   * @return the principal in the keytab specified by the {@link 
   * #getKeytabPath()} that sohuld be used to establish an authenticated
   * Kerberos session with the FreeIPA server's KDC
   */
  public String getPrincipal() {
    return principal;
  }

  public void setPrincipal(String principal) {
    this.principal = principal;
  }
  
  /**
   * @return an optional {@link ObjectMapper} to supply to the {@link 
   * JsonRpcHttpClient} that will be used convert response from the 
   * FreeIPA instance's RPC API.
   */
  public ObjectMapper getRPCObjectMapper() {
    return this.rpcObjectMapper;
  }
  
  public void setRPCObjectMapper(ObjectMapper om) {
    this.rpcObjectMapper = om;
  }
}
