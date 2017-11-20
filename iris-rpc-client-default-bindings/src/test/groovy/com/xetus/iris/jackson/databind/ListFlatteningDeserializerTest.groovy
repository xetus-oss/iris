package com.xetus.oss.iris.jackson.databind

import org.junit.runners.Parameterized.Parameters

import com.xetus.oss.iris.jackson.databind.testmodel.ListFlattenedPojo

class ListFlatteningDeserializerTest 
      extends AbstractDeserlalizerTest<ListFlattenedPojo> {

  Class<ListFlattenedPojo> getExpectedClass() {
    return  ListFlattenedPojo.class
  }
        
  @Parameters(name = "flatten list testcase {index}: {0}")
  public static List<Object[]> getParameters() {
    List<Object[]> testCases = []
    
    testCases << ([
      "list<string> deserialization to string",
      '''{ "stringProp": [ "some value" ] }''',
      new ListFlattenedPojo(stringProp: "some value")
    ] as Object[])
    
    testCases << ([
      "list<integer> deserialization to integer",
      '''{ "integerProp": [ 999 ] }''',
      new ListFlattenedPojo(integerProp: 999)
    ] as Object[])
    
    testCases << ([
      "list<list> deserialization to list<string>",
      '''{ "stringListProp": [ [ "some value" ] ] }''',
      new ListFlattenedPojo(stringListProp: [ "some value" ])
    ] as Object[])
    
    testCases << ([
      "list<list> deserialization to list<integer>",
      '''{ "integerListProp": [ [ 999 ] ] }''',
      new ListFlattenedPojo(integerListProp: [ 999 ])
    ] as Object[])
    
    testCases << ([
      "list<map> deserialization to map<string, string>",
      '''{ "stringStringMapProp": [ { "some_prop": "some value" } ] }''',
      new ListFlattenedPojo(stringStringMapProp: [ some_prop: "some value" ])
    ] as Object[])
    
    testCases << ([
      "list<map> deserialization to map<string, integer>",
      '''{ "integerIntegerMapProp": [ { "999": 1000 } ] }''',
      new ListFlattenedPojo(integerIntegerMapProp: [ "999": 1000 ])
    ] as Object[])
    
    return testCases
  }
}
