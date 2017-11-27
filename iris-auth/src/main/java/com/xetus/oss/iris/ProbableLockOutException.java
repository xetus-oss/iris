package com.xetus.oss.iris;

public class ProbableLockOutException extends Exception {

  private static final long serialVersionUID = 1L;

  public ProbableLockOutException() {}

  public ProbableLockOutException(String message) {
    super(message);
  }

  public ProbableLockOutException(Throwable cause) {
    super(cause);
  }

  public ProbableLockOutException(String message, Throwable cause) {
    super(message, cause);
  }

  public ProbableLockOutException(String message, 
                                  Throwable cause,
                                  boolean enableSuppression, 
                                  boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
  
}