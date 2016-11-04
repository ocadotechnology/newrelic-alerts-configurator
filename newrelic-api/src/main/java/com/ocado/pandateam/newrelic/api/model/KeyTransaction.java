package com.ocado.pandateam.newrelic.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class KeyTransaction {
    @JsonProperty
    Integer id;
    @JsonProperty
    String name;
    @JsonProperty("transaction_name")
    String transactionName;
    @JsonProperty("health_status")
    String healthStatus;
    @JsonProperty
    Boolean reporting;
    @JsonProperty("last_reported_at")
    String lastReportedAt;
    @JsonProperty("application_summary")
    ApplicationSummary applicationSummary;
    @JsonProperty("links")
    KeyTransactionLinks links;
}
