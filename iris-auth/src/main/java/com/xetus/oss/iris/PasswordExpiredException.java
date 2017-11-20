package com.xetus.oss.iris;

class PasswordExpiredException extends Exception {

  private static final long serialVersionUID = 1L;

  public PasswordExpiredException() {}

  public PasswordExpiredException(String message) {
    super(message);
  }

  public PasswordExpiredException(Throwable cause) {
    super(cause);
  }

  public PasswordExpiredException(String message, Throwable cause) {
    super(message, cause);
  }

  public PasswordExpiredException(String message, 
                                Throwable cause,
                                boolean enableSuppression, 
                                boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
  
}
