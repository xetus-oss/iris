package com.xetus.iris

import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

import com.xetus.iris.model.RPCResponse
import com.xetus.iris.model.freeipa.account.PasswordPolicy

class FreeIPAClientPwPolicyShowResponseDeserializationTest
      extends AbstractFreeIPAClientMethodResponseDeserializationTest<PasswordPolicy> {
  
  @Override
  public RPCResponse<PasswordPolicy> getActual(FreeIPAClient client) {
    return client.pwpolicyShow()
  }

  @Parameters(name = "#pwPolicyShow testCase {index}: {0}")
  static List<Object[]> getParameters() {
    List<Object[]> testCases = []
    
    testCases << ([
      "successful pwdpolicy_show request",
      "/testdata/pwpolicy/pwpolicy-show_response-1.json",
      null,
      new RPCResponse<PasswordPolicy>().with { RPCResponse response ->
        messages = []
        result = new PasswordPolicy().with { PasswordPolicy pwPolicy ->
          attributeLevelRights = [
            "aci": "rscwo", 
            "cn": "rscwo", 
            "krbmaxpwdlife": "rscwo", 
            "krbminpwdlife": "rscwo", 
            "krbpwdfailurecountinterval": "rscwo", 
            "krbpwdhistorylength": "rscwo", 
            "krbpwdlockoutduration": "rscwo", 
            "krbpwdmaxfailure": "rscwo", 
            "krbpwdmindiffchars": "rscwo", 
            "krbpwdminlength": "rscwo", 
            "nsaccountlock": "rscwo", 
            "objectclass": "rscwo"
          ]
          cn = "global_policy"
          dn = "cn=global_policy,cn=DEV.XETUS.COM,cn=kerberos,dc=dev,dc=xetus,dc=com"
          krbMaxPwdLife = 90
          krbMinPwdLife = 1
          krbPwdFailureCountInterval = 60
          krbPwdHistoryLength = 0
          krbPwdLockoutDuration = 600
          krbPwdMaxFailure = 6
          krbPwdMinDiffChars = 0
          krbPwdMinLength = 8
          objectClass = [
            "nsContainer",
            "top",
            "krbPwdPolicy"
          ]
          
          return pwPolicy
        }
        return response
      }
    ] as Object[])
    
    return testCases
  }

}
