package com.xetus.oss.iris.cli;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xetus.oss.iris.FreeIPAConfig;
import com.xetus.oss.iris.jackson.databind.ObjectMapperBuilder;

@Component
@ConfigurationProperties(prefix = "iris")
public class IrisCliConfig extends FreeIPAConfig {
  protected ObjectMapper rpcObjectMapper = ObjectMapperBuilder.getObjectMapper();
}
