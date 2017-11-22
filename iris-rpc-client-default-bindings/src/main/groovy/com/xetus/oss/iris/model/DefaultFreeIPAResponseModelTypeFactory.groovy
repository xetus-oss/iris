package com.xetus.oss.iris.model

import com.xetus.oss.iris.model.freeipa.account.KerberosTicketPolicy
import com.xetus.oss.iris.model.freeipa.account.PasswordPolicy
import com.xetus.oss.iris.model.freeipa.account.User

import groovy.transform.CompileStatic

@CompileStatic
public class DefaultFreeIPAResponseModelTypeFactory
       implements FreeIPAResponseModelTypeFactory {

  Class<?> userClass = User.class;
  Class<?> kerberosTicketPolicyClass = KerberosTicketPolicy.class;
  Class<?> passwordPolicyClass = PasswordPolicy.class;

  DefaultFreeIPAResponseModelTypeFactory registerUserClass(Class<?> userClass) {
    this.userClass = userClass;
    return this;
  }

  DefaultFreeIPAResponseModelTypeFactory registerKerberosTicketPolicyClass(Class<?> krbTktPolicy) {
    this.kerberosTicketPolicyClass = krbTktPolicy;
    return this;
  }

  DefaultFreeIPAResponseModelTypeFactory registerPasswordPolicyClass(Class<?> pwPolicy) {
    this.passwordPolicyClass = pwPolicy;
    return this;
  }
}
