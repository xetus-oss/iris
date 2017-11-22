package com.xetus.oss.iris;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

public class FreeIPAConfig {
  
  public static final Logger LOGGER = LoggerFactory.getLogger(FreeIPAConfig.class);
  
  private String hostname;
  private String realm;
  private String keytabPath;
  private String krb5ConfigPath;
  private String principal;
  protected ObjectMapper rpcObjectMapper;
  
  /**
   * @return the hostname for the FreeIPA instance
   */
  public String getHostname() {
    return hostname;
  }

  /**
   * @param hostname the hostname for the FreeIPA instance
   */
  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  /**
   * @return the default realm used to authenticate against the FreeIPA
   * instance
   */
  public String getRealm() {
    return realm;
  }

  /**
   * @param realm the default realm that sohuld be used to authenticate
   * against the FreeIPA instance
   */
  public void setRealm(String realm) {
    this.realm = realm;
  }
  
  /**
   * @return the path to the Kerberos krb5 config that contains the 
   * requisite krb5 configuration information.
   */
  public String getKrb5ConfigPath() {
    return krb5ConfigPath;
  }
  
  /**
   * @param krb5ConfigPath The path to the krb5.conf file that should be
   * used during Kerberos authentications
   */
  public void setKrb5ConfigPath(String krb5ConfigPath) {
    File krb5Config = new File(krb5ConfigPath);
    String absPath = krb5Config.getAbsolutePath();
    if (!krb5Config.exists()) {
      throw new IllegalArgumentException(
          "Failed to locate krb5 conf at " + absPath
      );
    }
    this.krb5ConfigPath = absPath;
  }
  
  /**
   * @return the path to the Kerberos keytab that should be used to 
   * establish an authenticated Kerberos RPC session with the FreeIPA 
   * server's KDC.
   */
  public String getKeytabPath() {
    return keytabPath;
  }

  /**
   * @param keytabPath The path to the Kerberos keytab that should be
   * used to establsh an authenticated Kerberos RPC session with the
   * FreeIPA server's KDC
   */
  public void setKeytabPath(String keytabPath) {
    File keytab = new File(keytabPath);
    String absPath = keytab.getAbsolutePath();
    if (!keytab.exists()) {
      throw new IllegalArgumentException(
          "Failed to locate keytab at " + absPath
      );
    }
    this.keytabPath = absPath;
  }

  /**
   * @return the principal in the keytab specified by the {@link 
   * #getKeytabPath()} that sohuld be used to establish an authenticated
   * Kerberos session with the FreeIPA server's KDC
   */
  public String getPrincipal() {
    return principal;
  }

  /**
   * @param principal the principal in the keytab specified by the 
   * {@link #getKeytabPath()} that should be used to establish an 
   * authenticated Kerberos session with the FreeIPA server's KDC
   */
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

  /**
   * @param om the optional {@link ObjectMapper} to supply to the 
   * {@link JsonRpcHttpClient} that will be used to convert responses
   * from the FreeIPA instance's RPC API
   */
  public void setRPCObjectMapper(ObjectMapper om) {
    this.rpcObjectMapper = om;
  }

}
