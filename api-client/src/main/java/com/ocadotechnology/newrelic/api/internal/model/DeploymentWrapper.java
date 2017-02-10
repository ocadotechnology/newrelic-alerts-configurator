package com.ocadotechnology.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.api.model.deployments.Deployment;
import lombok.Value;

@Value
public class DeploymentWrapper {
    @JsonProperty
    Deployment deployment;
}
