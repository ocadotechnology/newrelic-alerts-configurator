package com.ocado.panda.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.panda.newrelic.api.model.ObjectList;
import com.ocado.panda.newrelic.api.model.deployments.Deployment;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class DeploymentsList extends ObjectList<Deployment, DeploymentsList> {
    @JsonCreator
    public DeploymentsList(@JsonProperty("deployments") List<Deployment> items) {
        super(items, DeploymentsList::new);
    }
}
