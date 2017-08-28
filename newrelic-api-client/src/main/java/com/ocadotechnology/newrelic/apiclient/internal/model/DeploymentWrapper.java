package com.ocadotechnology.newrelic.apiclient.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.apiclient.model.deployments.Deployment;
import lombok.Value;

@Value
public class DeploymentWrapper {
    @JsonProperty
    Deployment deployment;
}
