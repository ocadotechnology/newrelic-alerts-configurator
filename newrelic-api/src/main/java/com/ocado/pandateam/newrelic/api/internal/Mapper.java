package com.ocado.pandateam.newrelic.api.internal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mashape.unirest.http.ObjectMapper;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiRuntimeException;

import java.io.IOException;

class Mapper implements ObjectMapper {

    private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper;

    Mapper() {
        jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        jacksonObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jacksonObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jacksonObjectMapper.registerModule(new Jdk8Module());
        jacksonObjectMapper.registerModule(new JavaTimeModule());
    }

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
