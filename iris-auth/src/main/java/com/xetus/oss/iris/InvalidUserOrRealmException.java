package com.xetus.oss.iris;

/**
 * Can indicate a number of errors from the FreeIPA server, including:<ul>
 * 
 *  <li>Invalid user name
 *  <li>Invalid realm
 *  <li>Insufficient privileges/access rights
 *  
 * </ul>
 */
class InvalidUserOrRealmException extends Exception {

  private static final long serialVersionUID = 1L;

  public InvalidUserOrRealmException() {}

  public InvalidUserOrRealmException(String message) {
    super(message);
  }

  public InvalidUserOrRealmException(Throwable cause) {
    super(cause);
  }

  public InvalidUserOrRealmException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidUserOrRealmException(String message, 
                                Throwable cause,
                                boolean enableSuppression, 
                                boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
  
}
