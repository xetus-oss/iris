package com.xetus.iris

import groovy.transform.CompileStatic

@CompileStatic
class PasswordExpiredException extends Exception {

  public PasswordExpiredException() {
  }

  public PasswordExpiredException(String message) {
    super(message)
  }

  public PasswordExpiredException(Throwable cause) {
    super(cause)
  }

  public PasswordExpiredException(String message, Throwable cause) {
    super(message, cause)
  }

  public PasswordExpiredException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace)
  }
  
}
