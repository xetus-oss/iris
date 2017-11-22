package com.xetus.oss.iris;

public class InvalidPasswordException extends Exception {

  private static final long serialVersionUID = 1L;

  public InvalidPasswordException() {}

  public InvalidPasswordException(String message) {
    super(message);
  }

  public InvalidPasswordException(Throwable cause) {
    super(cause);
  }

  public InvalidPasswordException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidPasswordException(String message, 
                                Throwable cause,
                                boolean enableSuppression, 
                                boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
  
}
