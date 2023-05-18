package com.ocadotechnology.newrelic.graphql.apiclient.internal.transport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
public class Error {
    Extensions extensions;
    String message;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Value
    public static class Extensions {
        String code;
        String errorClass;
    }
}
