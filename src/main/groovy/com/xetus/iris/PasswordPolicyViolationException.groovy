package com.xetus.iris

import groovy.transform.CompileStatic

@CompileStatic
class PasswordPolicyViolationException extends Exception {

  public PasswordPolicyViolationException() {
  }

  public PasswordPolicyViolationException(String message) {
    super(message)
  }

  public PasswordPolicyViolationException(Throwable cause) {
    super(cause)
  }

  public PasswordPolicyViolationException(String message, Throwable cause) {
    super(message, cause)
  }

  public PasswordPolicyViolationException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace)
  }
  
}
