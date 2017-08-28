package com.ocadotechnology.newrelic.apiclient.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.apiclient.model.servers.Server;
import lombok.Value;

@Value
public class ServerWrapper {
    @JsonProperty
    Server server;
}
