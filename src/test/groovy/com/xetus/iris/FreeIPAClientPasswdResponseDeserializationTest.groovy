package com.xetus.iris

import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

import com.xetus.iris.model.RPCResponse

class FreeIPAClientPasswdResponseDeserializationTest 
      extends AbstractFreeIPAClientMethodResponseDeserializationTest<Boolean> {

  @Override
  public RPCResponse<Boolean> getActual(FreeIPAClient client) {
    return client.passwd("ignore", "ignore")
  }

  @Parameters(name = "#passwd testCase {index}: {0}")
  static List<Object[]> getParameters() {
    List<Object[]> testCases = []
    
    testCases << ([
      "successfull passwd request",
      "/testdata/passwd/passwd_response-1.json",
      null,
      new RPCResponse<Boolean>().with { RPCResponse response ->
        messages = []
        result = true
        summary = "Changed password for \"testuser@DEV.XETUS.COM\""
        value = "testuser@DEV.XETUS.COM"
        return response
      }
    ] as Object[])
    
    return testCases
  }

}
