package com.xetus.oss.iris;

public class InvalidKeytabException extends Exception {

  private static final long serialVersionUID = 1L;

  public InvalidKeytabException() {}

  public InvalidKeytabException(String message) {
    super(message);
  }

  public InvalidKeytabException(Throwable cause) {
    super(cause);
  }

  public InvalidKeytabException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidKeytabException(String message, 
                                Throwable cause,
                                boolean enableSuppression, 
                                boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
  
}
