package com.xetus.iris

import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

import com.googlecode.jsonrpc4j.JsonRpcClientException
import com.xetus.iris.model.RPCResponse
import com.xetus.iris.model.freeipa.account.User

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
          displayName = "TestUser User"
          dn = "uid=testuser,cn=users,cn=accounts,dc=dev,dc=xetus,dc=com"
          gecos = "TestUser User"
          gidnumber = "1092800001"
          givenName = "TestUser"
          hasKeytab = true
          hasPassword = true
          homeDirectory = "/home/testuser"
          initials = "TU"
          ipaUniqueId = "3e2270b8-a107-11e5-90dd-0242ac110005"
          krbExtraData = "AAKRdmxWcm9vdC9hZG1pbkBERVYuWEVUVVMuQ09NAA=="
          krbLastPwdChange = Date.parse("yyyy-MM-dd HH:mm:ss", "2015-12-12 19:33:37")
          krbPasswordExpiration = Date.parse("yyyy-MM-dd HH:mm:ss", "2015-12-12 19:33:37")
          krbPrincipalName = "testuser@DEV.XETUS.COM"
          loginShell = "/bin/sh"
          mail = "testuser@dev.xetus.com"
          memberOfGroup = ["ipausers"]
          mepManagedEntry = "cn=testuser,cn=groups,cn=accounts,dc=dev,dc=xetus,dc=com"
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
          uidNumber = "1092800001"
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
