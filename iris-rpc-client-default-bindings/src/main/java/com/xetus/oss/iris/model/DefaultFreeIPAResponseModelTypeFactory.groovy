package com.xetus.oss.iris.model

import com.xetus.oss.iris.model.freeipa.account.KerberosTicketPolicy;
import com.xetus.oss.iris.model.freeipa.account.PasswordPolicy;
import com.xetus.oss.iris.model.freeipa.account.User;

public class DefaultFreeIPAResponseModelTypeFactory
       implements FreeIPAResponseModelTypeFactory {

  private Class<?> userClass = User.class;
  private Class<?> kerberosTicketPolicyClass = KerberosTicketPolicy.class;
  private Class<?> passwordPolicyClass = PasswordPolicy.class;

  public DefaultFreeIPAResponseModelTypeFactory registerUserClass(Class<?> userClass) {
    this.userClass = userClass;
    return this;
  }

  public DefaultFreeIPAResponseModelTypeFactory registerKerberosTicketPolicyClass(Class<?> krbTktPolicy) {
    this.kerberosTicketPolicyClass = krbTktPolicy;
    return this;
  }

  public DefaultFreeIPAResponseModelTypeFactory registerPasswordPolicyClass(Class<?> pwPolicy) {
    this.passwordPolicyClass = pwPolicy;
    return this;
  }
}
