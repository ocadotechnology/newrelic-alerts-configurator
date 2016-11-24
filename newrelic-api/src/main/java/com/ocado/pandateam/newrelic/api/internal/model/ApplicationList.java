package com.ocado.pandateam.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.pandateam.newrelic.api.model.ObjectList;
import com.ocado.pandateam.newrelic.api.model.applications.Application;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class ApplicationList extends ObjectList<Application> {
    @JsonProperty("applications")
    List<Application> list;

    @Override
    public ApplicationList merge(ObjectList<Application> list) {
        return new ApplicationList(mergeList(list));
    }
}
