package com.xetus.iris

import static org.mockito.Mockito.when

import java.lang.reflect.Type
import java.util.Map

import org.mockito.Mockito

import com.googlecode.jsonrpc4j.JsonRpcHttpClient
import com.xetus.iris.jackson.databind.ObjectMapperBuilder

class MockUtils {
    
  public static JsonRpcHttpClient getMockRPCClient(String jsonResponse) { 
    
    /*
     * Generate a fake JSON request so the JsonRpcHttpClient's internals have
     * something to output
     */
    URL responseFile = MockUtils.class.getResource(jsonResponse)
    if (responseFile == null) {
      throw new IllegalArgumentException("Failed to locate test response file: "
        + jsonResponse)
    }
    
    System.out.println("repsonse: " + new File(responseFile.toURI()).text)
    JsonRpcHttpClient client = new JsonRpcHttpClient(
          ObjectMapperBuilder.getObjectMapper(),
          new URL("http://fake.url.com"),
          [:]) {
      @Override
      public Object invoke(String methodName, Object argument, Type returnType, Map<String, String> extraHeaders) throws Throwable {
        return super.readResponse(returnType, MockUtils.class.getResourceAsStream(jsonResponse))
      };
    }
    
    return client;
  }
  
  
  
}
