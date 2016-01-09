package com.xetus.iris.jackson.databind

import static org.junit.Assert.assertEquals
import groovy.json.JsonOutput

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameter

import com.fasterxml.jackson.databind.ObjectMapper

@RunWith(value = Parameterized.class)
abstract class AbstractDeserlalizerTest<T> {

  abstract Class<T> getExpectedClass()
  
  @Parameter(value = 0)
  public String testName
  
  @Parameter(value = 1)
  public String inputJson
  
  @Parameter(value = 2)
  public T expected
  
  @Test
  public void test() {
    System.out.println("Running test: " + testName)
    
    ObjectMapper mapper = new ObjectMapper()
    mapper.setTimeZone(TimeZone.getDefault())
    
    T actual = mapper.readValue(inputJson, getExpectedClass());
    
    System.out.println("expected type: " + expected?.getClass()
                     + "\nexpected class: " + getExpectedClass()?.getName()
                     + "\nexpected: " + JsonOutput.prettyPrint(JsonOutput.toJson(expected))
                     + "\nactual type: " + actual?.getClass()
                     + "\nactual: " + JsonOutput.prettyPrint(JsonOutput.toJson(actual)))
    
    assertEquals("unexpected deserialization", actual, expected)
    
  }
}
