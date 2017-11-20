package com.xetus.oss.iris.model

import groovy.transform.CompileStatic

@CompileStatic
public interface FreeIPAResponseModelTypeFactory {
  public Class<?> getUserClass();
  public Class<?> getKerberosTicketPolicyClass();
  public Class<?> getPasswordPolicyClass();
}
