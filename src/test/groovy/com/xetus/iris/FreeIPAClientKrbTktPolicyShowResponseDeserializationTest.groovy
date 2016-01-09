package com.xetus.iris

import org.junit.runners.Parameterized.Parameters

import com.xetus.iris.model.RPCResponse
import com.xetus.iris.model.freeipa.account.KerberosTicketPolicy

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
          aci = [
            "(targetfilter = \"(objectclass=krbpwdpolicy)\")(version 3.0;acl \"permission:System: Add Group Password Policy\";allow (add) groupdn = \"ldap:///cn=System: Add Group Password Policy,cn=permissions,cn=pbac,dc=dev,dc=xetus,dc=com\";)", 
            "(targetattr = \"createtimestamp || entryusn || krbdefaultencsalttypes || krbmaxrenewableage || krbmaxticketlife || krbsupportedencsalttypes || modifytimestamp || objectclass\")(targetfilter = \"(objectclass=krbticketpolicyaux)\")(version 3.0;acl \"permission:System: Read Default Kerberos Ticket Policy\";allow (compare,read,search) groupdn = \"ldap:///cn=System: Read Default Kerberos Ticket Policy,cn=permissions,cn=pbac,dc=dev,dc=xetus,dc=com\";)", 
            "(targetfilter = \"(objectclass=krbpwdpolicy)\")(version 3.0;acl \"permission:System: Delete Group Password Policy\";allow (delete) groupdn = \"ldap:///cn=System: Delete Group Password Policy,cn=permissions,cn=pbac,dc=dev,dc=xetus,dc=com\";)", 
            "(targetattr = \"krbmaxpwdlife || krbminpwdlife || krbpwdfailurecountinterval || krbpwdhistorylength || krbpwdlockoutduration || krbpwdmaxfailure || krbpwdmindiffchars || krbpwdminlength\")(targetfilter = \"(objectclass=krbpwdpolicy)\")(version 3.0;acl \"permission:System: Modify Group Password Policy\";allow (write) groupdn = \"ldap:///cn=System: Modify Group Password Policy,cn=permissions,cn=pbac,dc=dev,dc=xetus,dc=com\";)", 
            "(targetattr = \"cn || cospriority || createtimestamp || entryusn || krbmaxpwdlife || krbminpwdlife || krbpwdfailurecountinterval || krbpwdhistorylength || krbpwdlockoutduration || krbpwdmaxfailure || krbpwdmindiffchars || krbpwdminlength || modifytimestamp || objectclass\")(targetfilter = \"(objectclass=krbpwdpolicy)\")(version 3.0;acl \"permission:System: Read Group Password Policy\";allow (compare,read,search) groupdn = \"ldap:///cn=System: Read Group Password Policy,cn=permissions,cn=pbac,dc=dev,dc=xetus,dc=com\";)"
          ]
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
          krbDefaultEncSaltTypes = [
            "aes256-cts:special", 
            "des3-hmac-sha1:special", 
            "aes128-cts:special", 
            "arcfour-hmac:special"
          ]
          krbMaxRenewableAge = 604800
          krbMaxTicketLife = 86400
          krbSearchScope = 2
          krbSubTrees = ["dc=dev,dc=xetus,dc=com"]
          krbSupportedEncSaltTypes = [
            "aes256-cts:special", 
            "camellia256-cts-cmac:normal", 
            "camellia256-cts-cmac:special", 
            "aes128-cts:normal", 
            "aes128-cts:special", 
            "camellia128-cts-cmac:normal", 
            "arcfour-hmac:normal", 
            "camellia128-cts-cmac:special", 
            "aes256-cts:normal", 
            "des3-hmac-sha1:special", 
            "des3-hmac-sha1:normal", 
            "arcfour-hmac:special"
          ]
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
