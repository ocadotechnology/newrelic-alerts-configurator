package com.ocado.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.newrelic.api.model.applications.Application;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class ApplicationList extends ObjectList<Application, ApplicationList> {
    @JsonCreator
    public ApplicationList(@JsonProperty("applications") List<Application> items) {
        super(items, ApplicationList::new);
    }
}
