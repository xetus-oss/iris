package com.xetus.oss.iris.jackson.databind.testmodel

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.xetus.oss.iris.jackson.databind.PossiblyJsonTypedObjectDeserializer

@CompileStatic
@EqualsAndHashCode
class PossiblyJsonTypedPojo {
  
  @JsonDeserialize(using = PossiblyJsonTypedObjectDeserializer)
  String stringObj
  
  @JsonDeserialize(using = PossiblyJsonTypedObjectDeserializer)
  Integer integerObj
  
  @JsonDeserialize(using = PossiblyJsonTypedObjectDeserializer)
  Date dateObj
  
  @JsonDeserialize(using = PossiblyJsonTypedObjectDeserializer)
  List<String> stringListObj
  
  @JsonDeserialize(using = PossiblyJsonTypedObjectDeserializer)
  List<Integer> integerListObj
  
  @JsonDeserialize(using = PossiblyJsonTypedObjectDeserializer)
  Map<String, Integer> mapStringObj
}
