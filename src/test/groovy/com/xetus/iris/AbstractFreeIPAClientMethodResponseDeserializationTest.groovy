package com.xetus.iris

import static org.junit.Assert.assertEquals
import groovy.json.JsonOutput

import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameter

import com.xetus.iris.model.DefaultFreeIPAResponseModelTypeFactory
import com.xetus.iris.model.RPCResponse

@RunWith(value = Parameterized.class)
abstract class AbstractFreeIPAClientMethodResponseDeserializationTest<R> {

  @Rule
  public ExpectedException expectedException = ExpectedException.none()
  
  @Parameter(value = 0)
  public String testName
  
  @Parameter(value = 1)
  public String testResponseJsonFile
  
  @Parameter(value = 2)
  public Throwable exception
  
  @Parameter(value = 3)
  public RPCResponse<R> expected
  
  
 abstract RPCResponse<R> getActual(FreeIPAClient client)
  
  @Test
  void test() {
    FreeIPAClient client = new FreeIPAClient(
      MockUtils.getMockRPCClient(testResponseJsonFile),
      new DefaultFreeIPAResponseModelTypeFactory()
    )
    
    if (exception != null) {
      expectedException.expect(exception.class)
      expectedException.expectMessage(exception.getMessage())
    }
    
    RPCResponse<R> actual = getActual(client)
    System.out.println("expected type: " + expected?.getResult()?.getClass()
                     + "\nexpected: " + JsonOutput.prettyPrint(JsonOutput.toJson(expected))
                     + "\nactual type: " + actual?.getResult()?.getClass()
                     + "\nactual: " + JsonOutput.prettyPrint(JsonOutput.toJson(actual)))
    
    assertEquals("Unexpected result", expected, actual)
  }
  
}
