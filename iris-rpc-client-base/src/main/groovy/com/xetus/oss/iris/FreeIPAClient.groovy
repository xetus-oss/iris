package com.xetus.oss.iris

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import java.lang.reflect.ParameterizedType

import org.apache.commons.lang3.reflect.TypeUtils

import com.googlecode.jsonrpc4j.JsonRpcClientException
import com.googlecode.jsonrpc4j.JsonRpcHttpClient

import com.xetus.oss.iris.model.FreeIPAResponseModelTypeFactory
import com.xetus.oss.iris.model.RPCResponse

/**
 * A wrapper around the jsonrpc4j library to simplify making
 * FreeIPA JSON-RPC calls.
 */
@Slf4j
@CompileStatic
class FreeIPAClient {
  
  private static final String DEFAULT_RPC_VERSION = "2.114"
  
  JsonRpcHttpClient rpcClient
  FreeIPAResponseModelTypeFactory typeFactory
  String rpcVersion
  
  FreeIPAClient(JsonRpcHttpClient client, 
                FreeIPAResponseModelTypeFactory typeFactory) {
    this(client, typeFactory, DEFAULT_RPC_VERSION)
  }
  
  FreeIPAClient(JsonRpcHttpClient client, 
                FreeIPAResponseModelTypeFactory typeFactory, 
                String rpcVersion) {
    this.rpcClient = client
    this.typeFactory = typeFactory
    this.rpcVersion = rpcVersion
  }
  
  /**
   * A wrapper around the {@link 
   * JsonRpcHttpClient#invoke(String, Object, java.lang.reflect.Type)} method.
   * If this does not expose satisfactory flexibility, consumers can always
   * retrieve the {@link JsonRpcHttpClient} directly through the
   * {@link #rpcClient} field.
   * 
   * This method should be used over the {@link #invokeList(String, List)} 
   * methods in cases where the expected FreeIPA JSON RPC response result
   * object is expected to be an unwrapped single object rather than a list
   * of objects.
   *  
   * @param method
   * @param flags
   * @param params
   * @param resultType
   * @return
   * 
   * @throws JsonRpcClientException if the server API responds with any
   * application errors
   */
  public <T> RPCResponse<T> invoke(String method, 
                                   List<String> flags, 
                                   Map<String, String> params,
                                   Class<T> resultType) throws JsonRpcClientException {
    
    log.trace("Issuing JSON-RPC request:\n\n"
      + "method: $method\n"
      + "flags: $flags\n"
      + "params: $params\n"
      + "--------\n\n")
    
    params << [ "version": rpcVersion ]
    ParameterizedType type = TypeUtils
        .parameterize(RPCResponse.class, resultType)
    (RPCResponse<T>) rpcClient.invoke(method, [flags, params], type)
  }
  
  /**
   * A wrapper around the {@link 
   * JsonRpcHttpClient#invoke(String, Object, Class)} method. If this does
   * not expose satisfactory fleixbility, consumers can always retrieve the
   * {@link JsonRpcHttpClient} diretly through the {@link #rpcClient} field.
   * 
   * This method should be used over the {@link #invoke(String, List)} 
   * methods in cases where the expected FreeIPA JSON RPC response result
   * object is expected to be a list of objects.
   * 
   * @param method
   * @param flags
   * @param params
   * @param resultType
   * @return
   * 
   * @throws JsonRpcClientException if the server API responds with any
   * application errors
   */
  public <T> RPCResponse<List<T>> invokeList(String method,
                                             List<String> flags = [], 
                                             Map<String, String> params = [:], 
                                             Class<T> resultType) throws JsonRpcClientException {
                                             
    params << [ "version": rpcVersion ]
    ParameterizedType type = TypeUtils.parameterize(
        RPCResponse.class,
        TypeUtils.parameterize(List.class, resultType)
    )
    (RPCResponse<List<T>>) rpcClient.invoke(method, [flags, params], type)
  } 
  
  /**
   * Proxies the `ipa user_find` command.
   * 
   * @param flags
   * @param params
   * @return
   * 
   * @throws JsonRpcClientException if the server API responds with any
   * application errors. Specifically: <ul>
   * 
   * </ul>
   */
  RPCResponse<List<? extends Object>> userFind(List<String> flags = [], 
                                Map<String, String> params = [:]) throws JsonRpcClientException {
    invokeList("user_find", flags, params, typeFactory.getUserClass())
  }
  
  /**
   * Proxies the `ipa user_show` command.
   * 
   * @param user
   * @return
   *
   * @throws JsonRpcClientException if the server API responds with any
   * application errors. Specifically: <ul>
   * 
   * </ul> 
   */
  RPCResponse<? extends Object> userShow(String user = null, 
                             Map<String, String> params = [:]) throws JsonRpcClientException {
    List<String> flags = ((user == null) ? (List<String>) [] : [user])
    invoke("user_show", flags, params, typeFactory.getUserClass())
  }
  
  /**
   * Proxies the `ipa user_show` command.
   * 
   * @param user
   * @return
   *
   * @throws JsonRpcClientException if the server API responds with any
   * application errors. Specifically: <ul>
   * 
   * </ul> 
   */
  RPCResponse<? extends Object> pwpolicyShow(Map<String, String> params = [:])
                              throws JsonRpcClientException {
    invoke("pwpolicy_show", [], params, typeFactory.getPasswordPolicyClass())
  }
  
  /**
   * Proxies the `ipa krbtpolicy_show` command.
   * 
   * @param user
   * @return
   *
   * @throws JsonRpcClientException if the server API responds with any
   * application errors. Specifically: <ul>
   * 
   * </ul> 
   */
  RPCResponse<? extends Object> krbtpolicyShow(String user = null, 
                                                   Map<String, String> params = [:]) throws JsonRpcClientException {
    List<String> flags = user == null ? (List<String>) [] : [user]
    invoke("krbtpolicy_show", flags, params, typeFactory.getKerberosTicketPolicyClass())
  }

  /**
   * Proxies the `ipa passwd` command. This version should be used if
   * the user is resetting their own password
   * 
   * @param user
   * @param oldPass
   * @param newPass
   * @return
   * 
   * @throws JsonRpcClientException if the server API responds with any
   * application errors. Specifically: <ul>
   *  <li>code 2100 indicates Invalid credentials
   *  <li>code 4203 indicates a password policy violation
   * </ul>
   */
  RPCResponse<Boolean> passwd(String user, String oldPass, String newPass) 
                       throws JsonRpcClientException {
    invoke(
      "passwd", 
      [user], 
      [current_password: oldPass, password: newPass], 
      Boolean.class
    )
  }

  /**
   * Proxies the `ipa passwd` command. This version should be used if
   * an administrative user is resetting a user's password
   * 
   * @param user
   * @param newPass
   * @return
   * 
   * @throws JsonRpcClientException if the server API responds with any
   * application errors. Specifically: <ul>
   *  <li>code 2100 indicates Invalid credentials
   *  <li>code 4203 indicates a password policy violation
   * </ul>
   */
  RPCResponse<Boolean> passwd(String user, String newPass)
                       throws JsonRpcClientException {
    invoke(
      "passwd", 
      [user], 
      [password: newPass], 
      Boolean.class
    )
  }
  
  /**
   * Proxies the `ipa user_add` command. Note that the session must have
   * been established for a user with sufficient privileges, and that the
   * explicit parameters are required by every FreeIPA instance.
   * 
   * @param uid
   * @param givenName
   * @param sn
   * @param attributes
   * @return
   * 
   * @throws JsonRpcClientException if the server API responds with any
   * application errors. Specifically: <ul>
   * </ul>
   */
  RPCResponse<? extends Object> userAdd(String uid, String givenName, 
                            String sn, 
                            Map<String, String> attributes = [:]) throws JsonRpcClientException {
    attributes.putAll([uid: uid, givenname: givenName, sn: sn])
    invoke("user_add",[], attributes, typeFactory.getUserClass())
  }
      
  RPCResponse<Boolean> logout() throws JsonRpcClientException {
    invoke("session_logout", [], [:], Boolean.class)
  }
}
