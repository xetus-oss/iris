package com.xetus.oss.iris.jackson.databind

import com.fasterxml.jackson.databind.ObjectMapper;

import groovy.transform.CompileStatic;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.MapperFeature;

@CompileStatic
class ObjectMapperBuilder {
  
  private static ObjectMapper mapper

  public static ObjectMapper getObjectMapper() {
    if (mapper == null) {
      mapper = new ObjectMapper()
      mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
      mapper.setDateFormat(new SimpleDateFormat("yyyyMMddHHmmss"))
    }
    return mapper
  }
  
}
