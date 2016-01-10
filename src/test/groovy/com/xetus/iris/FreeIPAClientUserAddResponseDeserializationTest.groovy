package com.xetus.iris

import groovy.transform.CompileStatic

import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

import com.googlecode.jsonrpc4j.JsonRpcClientException
import com.xetus.iris.model.RPCResponse
import com.xetus.iris.model.freeipa.account.User

@CompileStatic
class FreeIPAClientUserAddResponseDeserializationTest 
      extends AbstractFreeIPAClientMethodResponseDeserializationTest<User> {

  @Override
  public RPCResponse<User> getActual(FreeIPAClient client) {
    return client.userAdd("ignored", "ignored", "ignored")
  }
  
  @Parameters(name = "#userAdd testCase {index}: {0}")
  static List<Object[]> getParameters() {
    List<Object[]> testCases = []
    
    testCases << ([
      "#simple user add",
      "/testdata/user_add/user-add_response-1.json",
      null,
      new RPCResponse<User>().with { RPCResponse response ->
        messages = []
        summary = "Added user \"testuser\""
        value = "testuser"
        result = new User().with { User user ->
          cn = "TestUser User"
          dn = "uid=testuser,cn=users,cn=accounts,dc=dev,dc=xetus,dc=com"
          memberOfGroup = ["ipausers"]
          objectClass = [
            "ipaSshGroupOfPubKeys", 
            "ipaobject", 
            "mepOriginEntry", 
            "person", 
            "top", 
            "ipasshuser", 
            "inetorgperson", 
            "organizationalperson", 
            "krbticketpolicyaux", 
            "krbprincipalaux", 
            "inetuser", 
            "posixaccount"
          ]
          sn = "User"
          uid = "testuser"
          return user
        }
        return response
      }
    ] as Object[])
    
    testCases << ([
      "attempt to add duplicate entry",
      "/testdata/user_add/user-add_duplicate-user-response-1.json",
      new JsonRpcClientException(4002, "user with name \"testuser\" already exists", null),
      null
    ] as Object[])
    
    return testCases
  }

}
