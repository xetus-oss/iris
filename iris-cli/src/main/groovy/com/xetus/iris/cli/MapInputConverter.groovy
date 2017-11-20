package com.xetus.oss.iris.cli

import groovy.transform.CompileStatic

import asg.cliche.InputConverter

@CompileStatic
class MapInputConverter implements InputConverter {

  @Override
  public Object convertInput(String original, Class toClass) throws Exception {
    if (toClass.equals(Map.class)) {
      Map result = [:]
      original.split(",").each { String entryStr ->
        String[] entry = entryStr.split(":")
        result[entry[0]] = entry[1]
      }
      return result
    }
    return null
  }

}
