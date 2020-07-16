package com.ocadotechnology.newrelic.apiclient.model.dashboards.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class DashboardMetadata {

    @JsonProperty
    Integer version;
}
