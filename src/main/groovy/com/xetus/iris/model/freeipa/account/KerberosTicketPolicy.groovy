package com.xetus.iris.model.freeipa.account

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.xetus.iris.jackson.databind.ListFlatteningDeserializer

@CompileStatic
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
class KerberosTicketPolicy implements FreeIPARightsAware {
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String cn
  String dn
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  Integer krbMaxRenewableAge
  @JsonDeserialize(using = ListFlatteningDeserializer)
  Integer krbMaxTicketLife
  @JsonDeserialize(using = ListFlatteningDeserializer)
  Integer krbSearchScope
  
  List<String> objectClass
  
}
