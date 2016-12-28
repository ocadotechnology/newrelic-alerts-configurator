package com.ocado.panda.newrelic.api.model.servers;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ServerLinks {
    @JsonProperty("alert_policy")
    Integer alertPolicy;
}
