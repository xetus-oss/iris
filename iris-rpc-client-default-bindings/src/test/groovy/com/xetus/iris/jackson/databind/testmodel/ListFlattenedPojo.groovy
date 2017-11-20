package com.xetus.oss.iris.jackson.databind.testmodel

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.xetus.oss.iris.jackson.databind.ListFlatteningDeserializer

@CompileStatic
@EqualsAndHashCode
class ListFlattenedPojo {
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String stringProp
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  Integer integerProp
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  List<String> stringListProp
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  List<Integer> integerListProp
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  Map<String, String> stringStringMapProp
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  Map<String, Integer> integerIntegerMapProp

}
