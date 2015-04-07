package com.xetus.iris

import groovy.transform.CompileStatic

@CompileStatic
class InvalidPasswordException extends Exception {

  public InvalidPasswordException() {
  }

  public InvalidPasswordException(String message) {
    super(message)
  }

  public InvalidPasswordException(Throwable cause) {
    super(cause)
  }

  public InvalidPasswordException(String message, Throwable cause) {
    super(message, cause)
  }

  public InvalidPasswordException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace)
  }
}
