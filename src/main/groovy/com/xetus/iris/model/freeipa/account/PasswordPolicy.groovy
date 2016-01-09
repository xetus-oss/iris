package com.xetus.iris.model.freeipa.account

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.xetus.iris.jackson.databind.ListFlatteningDeserializer

@CompileStatic
@EqualsAndHashCode
class PasswordPolicy implements FreeIPARightsAware {

  Map<String, String> attributeLevelRights
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String cn
  String dn
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String krbMaxPwdLife
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String krbMinPwdLife
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String krbPwdFailureCountInterval
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String krbPwdHistoryLength
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String krbPwdLockoutDuration
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String krbPwdMaxFailure
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String krbPwdMinDiffChars
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String krbPwdMinLength
  
  List<String> objectClass
  
}
