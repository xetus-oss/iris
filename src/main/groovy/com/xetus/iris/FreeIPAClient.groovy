package com.xetus.iris

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import com.googlecode.jsonrpc4j.JsonRpcClientException
import com.googlecode.jsonrpc4j.JsonRpcHttpClient

/**
 * A wrapper around the jsonrpc4j library to simplify making
 * FreeIPA JSON-RPC calls. Note that this is probably a bad idea.   
 */
@Slf4j
@CompileStatic
class FreeIPAClient {

  JsonRpcHttpClient rpcClient
  
  FreeIPAClient(JsonRpcHttpClient client) {
    this.rpcClient = client
  }
  
  /**
   * A wrapper around the {@link 
   * JsonRpcHttpClient#invoke(String, Object, java.lang.reflect.Type)} method.
   * If this does not expose satisfactory flexibility, consumers can always
   * retrieve the {@link JsonRpcHttpClient} directly through the
   * {@link #rpcClient} field.
   * 
   * @param method
   * @param flags
   * @param params
   * @return
   * 
   * @throws JsonRpcClientException if the server API responds with any
   * application errors. Specifically: <ul>
   * 
   * </ul>
   */
  RPCResult invoke(String method, List<String> flags, 
                   Map<String, String> params)
      throws JsonRpcClientException {
    
    log.trace("Issuing JSON-RPC request:\n\n"
      + "method: $method\n"
      + "flags: $flags\n"
      + "params: $params\n"
      + "--------\n\n")
    
    def result = rpcClient.invoke(method, [flags, params], RPCResult.class)
    result
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
  RPCResult userFind(List<String> flags = [], Map<String, String> params = [:])
      throws JsonRpcClientException {
    invoke("user_find", flags, params)
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
  RPCResult userShow(String user, Map<String, String> params = [:]) {
    invoke("user_show", [user], params)
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
  RPCResult pwpolicyShow(Map<String, String> params = [:]) {
    invoke("pwpolicy_show", [], params)
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
  RPCResult krbtpolicyShow(String user, Map<String, String> params = [:]) {
    invoke("krbtpolicy_show", [user], params)
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
  RPCResult passwd(String user, String oldPass, String newPass) 
      throws JsonRpcClientException {
    invoke("passwd", [user], [current_password: oldPass, password: newPass])
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
  RPCResult passwd(String user, String newPass)
      throws JsonRpcClientException {
    invoke("passwd", [user], [password: newPass])
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
  RPCResult userAdd(String uid, String givenName, 
                    String sn, Map<String, String> attributes = [:])
      throws JsonRpcClientException {
    attributes.putAll([uid: uid, givenname: givenName, sn: sn])
    invoke("user_add", [], attributes)
  }
      
  RPCResult logout() throws JsonRpcClientException {
    invoke("session_logout", [], [:])
  }
}
