package com.ocadotechnology.newrelic.apiclient.model.conditions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.Collection;

/**
 * See <a href="https://rpm.newrelic.com/api/explore/alerts_nrql_conditions/list">Doc</a>
 * <p>
 */
@Value
@Builder
@AllArgsConstructor
@NonFinal
public class AlertsNrqlCondition {
    @JsonProperty
    Integer id;
    @JsonProperty
    String name;
    @JsonProperty
    Boolean enabled;
    @JsonProperty("runbook_url")
    String runbookUrl;
    @JsonProperty
    @Singular
    Collection<Terms> terms;
    @JsonProperty("value_function")
    String valueFunction;
    @JsonProperty
    Nrql nrql;
}
