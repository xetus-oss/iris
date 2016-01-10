package com.xetus.iris.model.freeipa.account

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.builder.Builder

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.xetus.iris.jackson.databind.ListFlatteningDeserializer

@Builder
@CompileStatic
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
class User implements FreeIPARightsAware{
    
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String cn
  
  String dn
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String sn
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String uid
  
  @JsonProperty(value = "memberof_group")
  List<String> memberOfGroup
  
  List<String> objectClass
}
