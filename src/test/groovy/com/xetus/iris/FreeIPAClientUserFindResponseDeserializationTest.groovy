package com.xetus.iris

import groovy.transform.CompileStatic

import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

import com.xetus.iris.model.RPCResponse
import com.xetus.iris.model.freeipa.account.User

@CompileStatic
class FreeIPAClientUserFindResponseDeserializationTest 
      extends AbstractFreeIPAClientMethodResponseDeserializationTest<List<User>> {
  
  @Override
  public RPCResponse<User> getActual(FreeIPAClient client) {
    return client.userFind()
  }

  @Parameters(name = "#userFind testCase {index}: {0}")
  static List<Object[]> getParameters() {
    List<Object[]> testCases = []
    
    testCases << ([
      "simple user found",
      "/testdata/user_find/user-find_response-1.json",
      null,
      new RPCResponse<List<User>>().with { RPCResponse response ->
        count = 1
        messages = []
        summary = "1 user matched"
        truncated = false
        result = [
          new User().with { User user ->
            dn = "uid=admin,cn=users,cn=accounts,dc=dev,dc=xetus,dc=com"
            sn = "Administrator"
            uid = "admin"
            return user
          }
        ]
        return response
      }
    ] as Object[])
    
    return testCases
  }
}
