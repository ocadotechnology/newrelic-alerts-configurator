package com.ocado.newrelic.api.model.applications;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class ApplicationLinks {
    @JsonProperty("alert_policy")
    Integer alertPolicy;
    @JsonProperty
    List<Integer> servers;
    @JsonProperty("application_hosts")
    List<Integer> hosts;
    @JsonProperty("application_instances")
    List<Integer> instances;
}
