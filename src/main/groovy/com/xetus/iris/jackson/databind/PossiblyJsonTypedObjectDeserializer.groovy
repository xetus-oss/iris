package com.xetus.iris.jackson.databind

import groovy.transform.CompileStatic

import java.lang.reflect.ParameterizedType

import org.apache.commons.lang3.ClassUtils
import org.apache.commons.lang3.reflect.TypeUtils

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.ObjectCodec

import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.ContextualDeserializer

/**
 * A {@link JsonDeserializer} subclass that exposes a few analytical behaviors
 * during deserialization that are helpful when dealing with the FreeIPA JSON
 * RPC API:
 * 
 *  1. handles typed JSON values as they are sent by the FreeIPA JSON RPC
 *  API, although the type information sent by FreeIPA is ignored in favor
 *  of the corresponding property's type. For example:
 *  
 *    @JsonDeserialize(using = PossiblyJsonTypedObjectDeserializer)
 *    Date example
 *    
 *  will attempt to convert both of the following to Date objects (the second 
 *  will fail due to a parse exception): 
 *    
 *    (will succeed)
 *    "example": {
 *        "__datetime__": "20151212193337Z"
 *    }
 *    
 *    (will fail)
 *    "example": {
 *        "__base64__": "AAKRdmxWcm9vdC9hZG1pbkBERVYuWEVUVVMuQ09NAA=="
 *    }
 *    
 *    
 *  2. handles unwrapping an object if necessary. For example, certain items
 *  always seem to be wrapped in a JSON array:
 *  
 *    
 */
@CompileStatic
class PossiblyJsonTypedObjectDeserializer<T> extends JsonDeserializer<T>
                                             implements ContextualDeserializer {
  
  Class<T> targetClass
  
  @Override
  public T deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    ObjectCodec oc = p.getCodec()
    ObjectMapper mapper = (ObjectMapper) oc
    JsonNode n = (JsonNode) oc.readTree(p)

    Object value = unwrapAndGetUntypeValue(n, targetClass, mapper) 
    return mapper.convertValue(value, targetClass)
  }
  
  private Object unwrapAndGetUntypeValue(JsonNode node, Class expectedClass, ObjectMapper mapper) {
    if (node.asToken() != JsonToken.START_ARRAY) {
      return getUnTypedValue(node, expectedClass, mapper)
    }
    
    if (node.size() != 1 ||
        (ClassUtils.isAssignable(List.class, expectedClass) &&
         node[0].asToken() != JsonToken.START_ARRAY)) {
        
        if (node[0].asToken() == JsonToken.START_OBJECT) {
          Object childUntypedResult = getUnTypedValue(node[0], expectedClass, mapper)
          if (node != childUntypedResult) {
            return childUntypedResult
          }
        }
       
      return getUnTypedValue(node, expectedClass, mapper)
    }
    
    return getUnTypedValue(node[0], expectedClass, mapper)
  }
  
  private Object getUnTypedValue(JsonNode node, Class expectedClass, ObjectMapper mapper) {
    if (node.asToken() != JsonToken.START_OBJECT) {
      return node
    }
    
    if (node.size() != 1 ||
        (ClassUtils.isAssignable(Map.class, expectedClass) &&
         node.iterator().next()?.asToken() != JsonToken.START_OBJECT)) {
     return node
    }
     
    ParameterizedType type = TypeUtils.parameterize(Map.class, String.class, T.class)
    Map<String, T> m = mapper.treeToValue(
      node,
      (Class<Map<String, T>>)  TypeUtils.getRawType(type, null)
    )
    
    return m.size() == 1 ? m.values()[0] : null
  }
  @Override
  public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
      BeanProperty property) throws JsonMappingException {
    targetClass = property.getType().getRawClass()
    return this
  }
}
