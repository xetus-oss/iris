package com.xetus.oss.iris.model.freeipa.account

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.xetus.oss.iris.jackson.databind.ListFlatteningDeserializer

@CompileStatic
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
class PasswordPolicy implements FreeIPARightsAware {

  @JsonDeserialize(using = ListFlatteningDeserializer)
  String cn
  String dn
  
  List<String> objectClass
  
}
