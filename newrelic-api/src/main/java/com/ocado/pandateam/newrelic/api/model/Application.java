package com.ocado.pandateam.newrelic.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class Application {
    @JsonProperty
    Integer id;
    @JsonProperty
    String name;
    @JsonProperty
    String language;
    @JsonProperty("health_status")
    String healthStatus;
    @JsonProperty
    Boolean reporting;
    @JsonProperty("last_reported_at")
    OffsetDateTime lastReportedAt;
    @JsonProperty("application_summary")
    ApplicationSummary summary;
    @JsonProperty
    ApplicationSettings settings;
    @JsonProperty
    ApplicationLinks links;
}
