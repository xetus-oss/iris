package com.xetus.oss.iris;

class SessionExpiredException extends Exception {

  private static final long serialVersionUID = 1L;

  public SessionExpiredException() {}

  public SessionExpiredException(String message) {
    super(message);
  }

  public SessionExpiredException(Throwable cause) {
    super(cause);
  }

  public SessionExpiredException(String message, Throwable cause) {
    super(message, cause);
  }

  public SessionExpiredException(String message, 
                                Throwable cause,
                                boolean enableSuppression, 
                                boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
  
}
