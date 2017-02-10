package com.ocadotechnology.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.api.model.servers.Server;
import lombok.Value;

@Value
public class ServerWrapper {
    @JsonProperty
    Server server;
}
