package com.xetus.oss.iris;

class PasswordPolicyViolationException extends Exception {

  private static final long serialVersionUID = 1L;

  public PasswordPolicyViolationException() {}

  public PasswordPolicyViolationException(String message) {
    super(message);
  }

  public PasswordPolicyViolationException(Throwable cause) {
    super(cause);
  }

  public PasswordPolicyViolationException(String message, Throwable cause) {
    super(message, cause);
  }

  public PasswordPolicyViolationException(String message, 
                                Throwable cause,
                                boolean enableSuppression, 
                                boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
  
}
