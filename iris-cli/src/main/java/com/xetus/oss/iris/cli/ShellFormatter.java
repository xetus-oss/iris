package com.xetus.oss.iris.cli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ShellFormatter {
  private ObjectMapper om = new ObjectMapper();
  public ShellFormatter() {}
  
  public String format(Object any) throws JsonProcessingException {
    return om.writeValueAsString(any);
  }
}
