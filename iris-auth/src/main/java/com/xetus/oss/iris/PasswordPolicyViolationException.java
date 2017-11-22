package com.xetus.oss.iris;

import java.util.ArrayList;
import java.util.List;

public class PasswordPolicyViolationException extends Exception {

  private static final long serialVersionUID = 1L;

  List<String> violations;
  
  public PasswordPolicyViolationException(List<String> violations) {
    this.violations = violations;
  }
  
  public List<String> getViolations() {
    return new ArrayList<>(violations);
  }
}
