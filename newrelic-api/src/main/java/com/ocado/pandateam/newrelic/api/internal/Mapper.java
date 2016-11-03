package com.ocado.pandateam.newrelic.api.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.ObjectMapper;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiRuntimeException;

import java.io.IOException;

public class Mapper implements ObjectMapper {
    private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
            = new com.fasterxml.jackson.databind.ObjectMapper();

    public <T> T readValue(String value, Class<T> valueType) {
        try {
            return jacksonObjectMapper.readValue(value, valueType);
        } catch (IOException e) {
            throw new NewRelicApiRuntimeException("Failed to deserialize JSON", e);
        }
    }

    public String writeValue(Object value) {
        try {
            return jacksonObjectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new NewRelicApiRuntimeException("Failed to serialize JSON", e);
        }
    }
}
