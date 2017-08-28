package com.ocadotechnology.newrelic.apiclient.model.deployments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class DeploymentLinks {
    @JsonProperty
    Integer application;
}
