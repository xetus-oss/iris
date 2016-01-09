package com.xetus.iris.model

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
@CompileStatic
class RPCResponse<R> {
  Integer count
  List<RPCMessage> messages
  R result
  Object value
  String summary
  Boolean truncated
}
