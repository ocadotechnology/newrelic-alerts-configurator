package com.ocadotechnology.newrelic.apiclient.model.applications;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

/**
 * See <a href="https://rpm.newrelic.com/api/explore/applications/list">Doc</a>
 */
@Value
@Builder
@AllArgsConstructor
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
    ApplicationSummary applicationSummary;
    @JsonProperty("end_user_summary")
    EndUserSummary endUserSummary;
    @JsonProperty
    ApplicationSettings settings;
    @JsonProperty
    ApplicationLinks links;
}
