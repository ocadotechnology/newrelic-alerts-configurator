package com.ocado.panda.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.panda.newrelic.api.model.deployments.Deployment;
import lombok.Value;

@Value
public class DeploymentWrapper {
    @JsonProperty
    Deployment deployment;
}
