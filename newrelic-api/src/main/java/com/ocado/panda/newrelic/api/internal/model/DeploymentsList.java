package com.ocado.panda.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.panda.newrelic.api.model.ObjectList;
import com.ocado.panda.newrelic.api.model.deployments.Deployment;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class DeploymentsList extends ObjectList<Deployment> {
    @JsonProperty("deployments")
    List<Deployment> list;

    @Override
    public DeploymentsList merge(ObjectList<Deployment> list) {
        return new DeploymentsList(mergeList(list));
    }
}
