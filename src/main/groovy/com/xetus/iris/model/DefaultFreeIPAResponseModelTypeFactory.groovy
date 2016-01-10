package com.xetus.iris.model

import groovy.transform.CompileStatic

import com.xetus.iris.model.freeipa.account.KerberosTicketPolicy
import com.xetus.iris.model.freeipa.account.PasswordPolicy
import com.xetus.iris.model.freeipa.account.User

@CompileStatic
public class DefaultFreeIPAResponseModelTypeFactory
       implements FreeIPAResponseModelTypeFactory {

  Class<?> userClass = User.class
  Class<?> kerberosTicketPolicyClass = KerberosTicketPolicy.class
  Class<?> passwordPolicyClass = PasswordPolicy.class

  public DefaultFreeIPAResponseModelTypeFactory registerUserClass(Class<?> userClass) {
    this.userClass = userClass
    return this
  }

  public DefaultFreeIPAResponseModelTypeFactory registerKerberosTicketPolicyClass(Class<?> krbTktPolicy) {
    this.kerberosTicketPolicyClass = krbTktPolicy
    return this
  }

  public DefaultFreeIPAResponseModelTypeFactory registerPasswordPolicyClass(Class<?> pwPolicy) {
    this.passwordPolicyClass = pwPolicy
    return this
  }
  

}
