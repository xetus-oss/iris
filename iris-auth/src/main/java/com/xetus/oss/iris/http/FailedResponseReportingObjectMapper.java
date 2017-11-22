package com.xetus.oss.iris.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

/**
 * An {@link ObjectMapper} that's been modified for the particular 
 * circumstances expected by the Iris library; namely, consumption in
 * a {@link JsonRpcHttpClient}, where only deserialization can be 
 * expected to happen through the {@link ObjectMapper#readValue(InputStream, com.fasterxml.jackson.databind.JavaType)}
 * method.
 */
public class FailedResponseReportingObjectMapper extends ObjectMapper {
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = LoggerFactory.getLogger(FailedResponseReportingObjectMapper.class);
  
  private String copyInputStream(InputStream src) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(src));
      return reader.lines().collect(Collectors.joining("\n"));
    }
    catch (Throwable e) {
      LOGGER.error("failed to read input stream", e);
      return null;
    }
  }

  /**
   * In the event the {@link JsonParser} used by the {@link ObjectMapper}
   * is supplied non-json, a {@link JsonParseException} is raised with 
   * the InputStream. However, by the time a consumer receives the 
   * exception, the {@link InputStream} has been closed -- and the 
   * internal exception handling means only the first character that
   * violates the possibilty of a JSON object is accessible from the 
   * exception.
   * 
   * This makes troubleshooting issues communicating with the FreeIPA
   * RPC API pretty painful. This method hangs on to the response text
   * and logs it at error level if the FreeIPA server response with
   * something other than JSON.
   */
  public <T> T readValue(InputStream src, Class<T> valueType)
                throws IOException, JsonParseException, JsonMappingException {
    String content = copyInputStream(src);
    InputStream streamCopy = new ByteArrayInputStream(content.getBytes());
    try {
      return super.readValue(streamCopy, valueType);
    } catch(JsonParseException pe) {
      LOGGER.error("JsonRPCClient failed to parse FreeIPA response: \n" + content);
      throw pe;
    }
  } 
}
