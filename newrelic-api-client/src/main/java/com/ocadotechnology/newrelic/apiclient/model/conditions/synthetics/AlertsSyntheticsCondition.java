package com.ocadotechnology.newrelic.apiclient.model.conditions.synthetics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.apiclient.model.PolicyItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * See <a href="https://rpm.newrelic.com/api/explore/alerts_nrql_conditions/list">Doc</a>
 */
@Value
@Builder
@AllArgsConstructor
@NonFinal
public class AlertsSyntheticsCondition implements PolicyItem {
    @JsonProperty
    Integer id;
    @JsonProperty
    String name;
    @JsonProperty("monitor_id")
    String monitorId;
    @JsonProperty
    Boolean enabled;
    @JsonProperty("runbook_url")
    String runbookUrl;
}
