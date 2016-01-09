package com.xetus.iris

import static org.mockito.Mockito.when

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
    OutputStream os = new ByteArrayOutputStream()
    os.write("{ \"jsonrpc\": 2.0, \"method\": \"fake\" }".getBytes("UTF-8"))
    HttpURLConnection con = Mockito.mock(HttpURLConnection.class)
    System.out.println("repsonse: " + new File(responseFile.toURI()).text)
    /*
     * Mock the JsonRpcHttpClient's connection so that no request is actually
     * issued, and:
     * 
     *  1. replace the OutputStream with the fake request (just to avoid
     *  internal errors, since we're not testing the output request here)
     *  
     *  2. replace the InputStream with the test data we want the 
     *  JsonRpcHttpClient to deserialize for us
     */
    when(con.getOutputStream()).thenReturn(os)
    when(con.getInputStream()).thenReturn(
      MockUtils.class.getResourceAsStream(jsonResponse))

    JsonRpcHttpClient client = new JsonRpcHttpClient(
          ObjectMapperBuilder.getObjectMapper(),
          new URL("http://dwww.butts.com"),
          [:]) {
      @Override
      protected HttpURLConnection prepareConnection(java.util.Map<String,String> extraHeaders) throws IOException {
        return con
      };
    }
    
    return client;
  }
  
  
  
}
