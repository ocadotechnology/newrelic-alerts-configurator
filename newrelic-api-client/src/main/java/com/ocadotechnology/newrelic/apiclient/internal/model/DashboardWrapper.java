package com.ocadotechnology.newrelic.apiclient.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.apiclient.model.dashboards.Dashboard;
import lombok.Value;

@Value
public class DashboardWrapper {
    @JsonProperty
    Dashboard dashboard;
}
