package com.ocadotechnology.newrelic.apiclient.internal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.apiclient.model.synthetics.Monitor;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class MonitorList extends ObjectList<Monitor, MonitorList> {
    @JsonCreator
    public MonitorList(@JsonProperty("monitors") List<Monitor> monitors) {
        super(monitors, MonitorList::new);
    }
}
