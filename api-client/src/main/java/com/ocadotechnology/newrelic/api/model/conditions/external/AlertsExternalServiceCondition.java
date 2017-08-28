package com.ocadotechnology.newrelic.api.model.conditions.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.api.model.conditions.Terms;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Collection;

/**
 * See <a href="https://rpm.newrelic.com/api/explore/alerts_external_service_conditions/list">Doc</a>
 */
@Value
@Builder
@AllArgsConstructor
public class AlertsExternalServiceCondition {
    @JsonProperty
    Integer id;
    @JsonProperty
    String type;
    @JsonProperty
    String name;
    @JsonProperty
    Boolean enabled;
    @JsonProperty
    @Singular
    Collection<Integer> entities;
    @JsonProperty
    String metric;
    @JsonProperty("runbook_url")
    String runbookUrl;
    @JsonProperty("external_service_url")
    String externalServiceUrl;
    @JsonProperty
    @Singular
    Collection<Terms> terms;
}
