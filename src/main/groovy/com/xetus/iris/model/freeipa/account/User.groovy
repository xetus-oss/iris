package com.xetus.iris.model.freeipa.account

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.builder.Builder

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.xetus.iris.jackson.databind.ListFlatteningDeserializer
import com.xetus.iris.jackson.databind.PossiblyJsonTypedObjectDeserializer

@Builder
@CompileStatic
@EqualsAndHashCode
class User implements FreeIPARightsAware{
    
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String cn
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String displayName
  
  String dn
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String gecos
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String gidnumber
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String givenName
  
  @JsonProperty(value = "has_keytab")
  Boolean hasKeytab
  @JsonProperty(value = "has_password")
  Boolean hasPassword
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String homeDirectory
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String initials
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String ipaUniqueId
  
  
  @JsonDeserialize(using = PossiblyJsonTypedObjectDeserializer)
  String krbExtraData
  @JsonDeserialize(using = PossiblyJsonTypedObjectDeserializer)
  Date krbLastFailedAuth
  @JsonDeserialize(using = PossiblyJsonTypedObjectDeserializer)
  Date krbLastPwdChange
  @JsonDeserialize(using = PossiblyJsonTypedObjectDeserializer)
  Date krbLastSuccessfulAuth
  
  @JsonDeserialize(using = PossiblyJsonTypedObjectDeserializer)
  Date krbLastAdminUnlock
  
  @JsonDeserialize(using = PossiblyJsonTypedObjectDeserializer)
  Integer krbLoginFailedCount
  @JsonDeserialize(using = PossiblyJsonTypedObjectDeserializer)
  Date krbPasswordExpiration
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String krbPrincipalName
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String loginShell
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String mail
  
  @JsonProperty(value = "memberof_group")
  List<String> memberOfGroup
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String mepManagedEntry
  
  Boolean nsAccountLock
  
  List<String> objectClass
  
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String sn
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String uid
  @JsonDeserialize(using = ListFlatteningDeserializer)
  String uidNumber
}
