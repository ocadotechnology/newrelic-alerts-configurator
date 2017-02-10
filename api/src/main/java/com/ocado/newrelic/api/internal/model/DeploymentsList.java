package com.ocado.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.newrelic.api.model.deployments.Deployment;
import com.ocado.newrelic.api.model.ObjectList;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class DeploymentsList extends ObjectList<Deployment, DeploymentsList> {
    @JsonCreator
    public DeploymentsList(@JsonProperty("deployments") List<Deployment> items) {
        super(items, DeploymentsList::new);
    }
}
