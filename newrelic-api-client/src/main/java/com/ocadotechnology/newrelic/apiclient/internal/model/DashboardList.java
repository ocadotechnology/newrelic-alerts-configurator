package com.ocadotechnology.newrelic.apiclient.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.apiclient.model.dashboards.Dashboard;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class DashboardList extends ObjectList<Dashboard, DashboardList> {
    @JsonCreator
    public DashboardList(@JsonProperty("dashboards") List<Dashboard> items) {
        super(items, DashboardList::new);
    }
}
