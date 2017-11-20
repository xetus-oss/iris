package com.xetus.oss.iris.jackson.databind

import org.junit.runners.Parameterized.Parameters

import com.xetus.oss.iris.jackson.databind.testmodel.PossiblyJsonTypedPojo

class PossiblyJsonTypedObjectDeserializerTest
      extends AbstractDeserlalizerTest<PossiblyJsonTypedPojo> {

  @Override
  public Class<PossiblyJsonTypedPojo> getExpectedClass() {
    return PossiblyJsonTypedPojo.class
  }
          
  @Parameters(name = "typed object testcase {index}: {0}")
  public static List<Object[]> getParameters() {
    List<Object[]> testCases = []
    
    /*
     * deserialize typed
     */
    
    testCases << ([
      "deserialize typed string object",
      '''{ "stringObj": { "__ignored__": "some string" } }''',
      new PossiblyJsonTypedPojo(stringObj: "some string")
    ] as Object[])
    
    testCases << ([
      "deserialize typed integer object",
      '''{ "integerObj": { "__ignored__": 999 } }''',
      new PossiblyJsonTypedPojo(integerObj: 999)
    ] as Object[])
    
    testCases << ([
      "deserialize typed date object",
      '''{ "dateObj": { "__ignored__": "2015-12-16" } }''',
      new PossiblyJsonTypedPojo(dateObj: Date.parse("yyyy-MM-dd", "2015-12-16"))
    ] as Object[])
    
    testCases << ([
      "deserialize typed list object",
      '''{ "stringListObj": { "__ignored__": [ "some string" ] } }''',
      new PossiblyJsonTypedPojo(stringListObj: [ "some string" ])
    ] as Object[])
    
    testCases << ([
      "deserialize typed list<integer> object",
      '''{ "integerListObj": { "__ignored__": [ 500 ] } }''',
      new PossiblyJsonTypedPojo(integerListObj: [ 500 ])
    ] as Object[])
    
    testCases << ([
      "deserialize typed map<string, integer> object",
      '''{ "mapStringObj": { "__ignored__": { "key": 500 } } }''',
      new PossiblyJsonTypedPojo(mapStringObj: [ "key": 500 ])
    ] as Object[])
    
    /*
     * deserialize wrapped and typed
     */
    
    testCases << ([
      "deserialize wrapped typed string object",
      '''{ "stringObj": [ { "__ignored__": "some string" } ] }''',
      new PossiblyJsonTypedPojo(stringObj: "some string")
    ] as Object[])
    
    testCases << ([
      "deserialize wrapped typed integer object",
      '''{ "integerObj": [ { "__ignored__": 999 } ] }''',
      new PossiblyJsonTypedPojo(integerObj: 999)
    ] as Object[])
    
    testCases << ([
      "deserialize wrapped typed date object",
      '''{ "dateObj": [ { "__ignored__": "2015-12-16" } ] }''',
      new PossiblyJsonTypedPojo(dateObj: Date.parse("yyyy-MM-dd", "2015-12-16"))
    ] as Object[])
    
    testCases << ([
      "deserialize wrapped typed list object",
      '''{ "stringListObj": [ { "__ignored__": [ "some string" ] } ] }''',
      new PossiblyJsonTypedPojo(stringListObj: [ "some string" ])
    ] as Object[])
    
    testCases << ([
      "deserialize wrapped typed list<integer> object",
      '''{ "integerListObj": [ { "__ignored__": [ 500 ] } ] }''',
      new PossiblyJsonTypedPojo(integerListObj: [ 500 ])
    ] as Object[])
    
    testCases << ([
      "deserialize wrapped typed map<string, integer> object",
      '''{ "mapStringObj": [ { "__ignored__": { "key": 500 } } ] }''',
      new PossiblyJsonTypedPojo(mapStringObj: [ "key": 500 ])
    ] as Object[])
    
    /*
     * deserialize un-typed
     */
    
    testCases << ([
      "deserialize un-typed string object",
      '''{ "stringObj": "some string" }''',
      new PossiblyJsonTypedPojo(stringObj: "some string")
    ] as Object[])
    
    testCases << ([
      "deserialize un-typed integer object",
      '''{ "integerObj": 999 }''',
      new PossiblyJsonTypedPojo(integerObj: 999)
    ] as Object[])
    
    testCases << ([
      "deserialize un-typed date object",
      '''{ "dateObj": "2015-12-16" }''',
      new PossiblyJsonTypedPojo(dateObj: Date.parse("yyyy-MM-dd", "2015-12-16"))
    ] as Object[])
    
    testCases << ([
      "deserialize un-typed list object",
      '''{ "stringListObj": [ "some string" ] }''',
      new PossiblyJsonTypedPojo(stringListObj: [ "some string" ])
    ] as Object[])
    
    testCases << ([
      "deserialize un-typed list<integer> object",
      '''{ "integerListObj": [ 500 ] }''',
      new PossiblyJsonTypedPojo(integerListObj: [ 500 ])
    ] as Object[])
    
    testCases << ([
      "deserialize un-typed map<string, integer> object",
      '''{ "mapStringObj": { "key": 500 } }''',
      new PossiblyJsonTypedPojo(mapStringObj: [ "key": 500 ])
    ] as Object[])
    return testCases
  }
}
