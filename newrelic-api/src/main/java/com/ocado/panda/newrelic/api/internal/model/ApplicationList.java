package com.ocado.panda.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.panda.newrelic.api.model.ObjectList;
import com.ocado.panda.newrelic.api.model.applications.Application;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class ApplicationList extends ObjectList<Application, ApplicationList> {
    @JsonCreator
    public ApplicationList(@JsonProperty("applications") List<Application> items) {
        super(items, ApplicationList::new);
    }
}
