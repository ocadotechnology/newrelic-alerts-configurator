package com.ocadotechnology.newrelic.graphql.apiclient.internal.transport;

import lombok.Getter;

import java.util.Arrays;

import static java.lang.String.format;

public enum AlertsWebhookCustomPayloadType {
    FORM("application/x-www-form-urlencoded"),
    JSON("application/json");

    @Getter
    private final String type;

    AlertsWebhookCustomPayloadType(String type) {
        this.type = type;
    }

    public static AlertsWebhookCustomPayloadType byType(String value) {
        return Arrays.stream(values()).filter(type -> type.getType().equalsIgnoreCase(value))
                .findFirst().orElseThrow(() -> new IllegalArgumentException(format("unknown payload type: '%s'", value)));
    }
}
