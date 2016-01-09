package com.xetus.iris.model.freeipa.account

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.xetus.iris.jackson.databind.ListFlatteningDeserializer

@CompileStatic
@EqualsAndHashCode
class KerberosTicketPolicy implements FreeIPARightsAware {
  
  List<String> aci
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String cn
  String dn
  List<String> krbDefaultEncSaltTypes
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  Integer krbMaxRenewableAge
  @JsonDeserialize(using = ListFlatteningDeserializer)
  Integer krbMaxTicketLife
  @JsonDeserialize(using = ListFlatteningDeserializer)
  Integer krbSearchScope
  
  List<String> krbSubTrees
  List<String> krbSupportedEncSaltTypes
  List<String> objectClass
  
}
