package com.xetus.iris.jackson.databind

import groovy.transform.CompileStatic

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.ObjectCodec

import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.ContextualDeserializer

@CompileStatic
class ListFlatteningDeserializer<T> extends JsonDeserializer<T>
                                    implements ContextualDeserializer {

  private Class<T> targetClass
                                      
  @Override
  public T deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

    ObjectCodec oc = p.getCodec()
    JsonNode node = (JsonNode) oc.readTree(p)

    ObjectMapper mapper = (ObjectMapper) p.getCodec()
    for (JsonNode n : node) {
      def value = mapper.convertValue(n, targetClass)
      return value
    }
    
    return null
  }

  @Override
  public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
      BeanProperty property) throws JsonMappingException {
    targetClass = property.getType().getRawClass()
    return this
  }
  
}
