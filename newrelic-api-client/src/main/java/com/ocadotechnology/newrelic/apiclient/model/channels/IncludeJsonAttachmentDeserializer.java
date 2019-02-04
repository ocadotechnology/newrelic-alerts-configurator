package com.ocadotechnology.newrelic.apiclient.model.channels;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class IncludeJsonAttachmentDeserializer extends JsonDeserializer<Boolean> {

    private static final Set<String> TRUTHY_VALUES = new HashSet<>(Arrays.asList("1", "true"));
    private static final Set<String> FALSY_VALUES = new HashSet<>(Arrays.asList("0", "false"));

    @Override
    public Boolean deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String value = jsonParser.readValueAs(String.class);

        return Optional.ofNullable(StringUtils.trimToNull(value))
                .map(val -> {
                    if (TRUTHY_VALUES.contains(val)) {
                        return true;
                    }
                    if (FALSY_VALUES.contains(val)) {
                        return false;
                    }
                    throw new RuntimeException("Unsupported field value: " + val);
                }).orElse(null);
    }
}
