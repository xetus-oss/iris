package com.xetus.iris

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
@CompileStatic
class RPCResult {
  Integer count
  List<RPCMessage> messages
  Object result
  Object value
  String summary
  Boolean truncated
}
