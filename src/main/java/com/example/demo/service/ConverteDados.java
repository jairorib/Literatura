package com.example.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class ConverteDados implements IConverteDados {
  private ObjectMapper mapper = new ObjectMapper();

  @Override
  public <T> T obterDados(String json, Class<T> classe) {
    try {
      JsonNode rootNode = mapper.readTree(json);
      JsonNode resultsNode = rootNode.get("results");
      String jsonString = resultsNode.toString();
      //mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
      return mapper.readValue(jsonString, classe);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

}
