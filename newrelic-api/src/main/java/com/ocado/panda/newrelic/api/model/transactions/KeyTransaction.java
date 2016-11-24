package com.ocado.panda.newrelic.api.model.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.panda.newrelic.api.model.applications.ApplicationLinks;
import com.ocado.panda.newrelic.api.model.applications.ApplicationSummary;
import com.ocado.panda.newrelic.api.model.applications.EndUserSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

/**
 * See <a href="https://rpm.newrelic.com/api/explore/key_transactions/list">Doc</a>
 */
@Value
@Builder
@AllArgsConstructor
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
    OffsetDateTime lastReportedAt;
    @JsonProperty("application_summary")
    ApplicationSummary applicationSummary;
    @JsonProperty("end_user_summary")
    EndUserSummary endUserSummary;
    @JsonProperty
    ApplicationLinks links;
}
