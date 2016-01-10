package com.xetus.iris

import groovy.transform.CompileStatic

import org.junit.runners.Parameterized.Parameters

import com.xetus.iris.model.RPCResponse
import com.xetus.iris.model.freeipa.account.KerberosTicketPolicy

@CompileStatic
class FreeIPAClientKrbTktPolicyShowResponseDeserializationTest
      extends AbstractFreeIPAClientMethodResponseDeserializationTest<KerberosTicketPolicy> {

  @Override
  public RPCResponse<KerberosTicketPolicy> getActual(FreeIPAClient client) {
    return client.krbtpolicyShow()
  }

  @Parameters(name = "#krbTktPolicyShow testCase {index}: {0}")
  static List<Object[]> getParameters() {
    List<Object[]> testCases = []
    
    testCases << ([
      "successful response behaves as expected",
      "/testdata/krbtpolicy/krbtpolicy-show_response-1.json",
      null,
      new RPCResponse<KerberosTicketPolicy>().with { RPCResponse response ->
        messages = []
        result = new KerberosTicketPolicy().with { KerberosTicketPolicy policy ->
          attributeLevelRights = [
            "aci": "rscwo", 
            "cn": "rscwo", 
            "krbadmservers": "rscwo", 
            "krbdefaultencsalttypes": "rscwo", 
            "krbkdcservers": "rscwo", 
            "krbldapservers": "rscwo", 
            "krbmaxrenewableage": "rscwo", 
            "krbmaxticketlife": "rscwo", 
            "krbmkey": "", 
            "krbprinccontainerref": "rscwo", 
            "krbprincnamingattr": "rscwo", 
            "krbpwdpolicyreference": "rscwo", 
            "krbpwdservers": "rscwo", 
            "krbsearchscope": "rscwo", 
            "krbsubtrees": "rscwo", 
            "krbsupportedencsalttypes": "rscwo", 
            "krbticketflags": "rscwo", 
            "krbticketpolicyreference": "rscwo", 
            "krbupenabled": "rscwo", 
            "nsaccountlock": "rscwo", 
            "objectclass": "rscwo"
          ]
          cn = "DEV.XETUS.COM"
          dn = "cn=DEV.XETUS.COM,cn=kerberos,dc=dev,dc=xetus,dc=com"
          krbMaxRenewableAge = 604800
          krbMaxTicketLife = 86400
          krbSearchScope = 2
          objectClass = [
            "krbrealmcontainer", 
            "top", 
            "krbticketpolicyaux"
          ]
          return policy
        }
        return response
      }
    ] as Object[])
    
    return testCases
  }

}
