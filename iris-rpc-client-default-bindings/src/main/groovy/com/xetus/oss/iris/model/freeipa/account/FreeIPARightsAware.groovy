package com.xetus.oss.iris.model.freeipa.account

import groovy.transform.CompileStatic

@CompileStatic
trait FreeIPARightsAware {
  Map<String, String> attributeLevelRights
}
