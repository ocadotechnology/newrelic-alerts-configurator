package com.ocado.panda.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.panda.newrelic.api.model.servers.Server;
import lombok.Value;

@Value
public class ServerWrapper {
    @JsonProperty
    Server server;
}
