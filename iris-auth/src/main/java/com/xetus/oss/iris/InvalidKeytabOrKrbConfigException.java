package com.xetus.oss.iris;

public class InvalidKeytabOrKrbConfigException extends Exception {

  private static final long serialVersionUID = 1L;

  public InvalidKeytabOrKrbConfigException() {}

  public InvalidKeytabOrKrbConfigException(String message) {
    super(message);
  }

  public InvalidKeytabOrKrbConfigException(Throwable cause) {
    super(cause);
  }

  public InvalidKeytabOrKrbConfigException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidKeytabOrKrbConfigException(String message, 
                                           Throwable cause,
                                           boolean enableSuppression, 
                                           boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
  
}
