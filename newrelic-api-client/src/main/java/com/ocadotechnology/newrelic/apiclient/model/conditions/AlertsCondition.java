package com.ocadotechnology.newrelic.apiclient.model.conditions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.Collection;

/**
 * See <a href="https://rpm.newrelic.com/api/explore/alerts_conditions/list">Doc</a>
 * <p>
 * Field {@link #conditionScope} is not documented in New Relic api explorer but it exists according to:
 * See <a href="https://docs.newrelic.com/docs/alerts/new-relic-alerts/configuring-alert-policies/scope-alert-thresholds-specific-instances">Link</a>
 */
@Value
@Builder
@AllArgsConstructor
@NonFinal
public class AlertsCondition {
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
    @JsonProperty("condition_scope")
    String conditionScope;
    @JsonProperty("runbook_url")
    String runbookUrl;
    @JsonProperty
    @Singular
    Collection<Terms> terms;
    @JsonProperty("violation_close_timer")
    String violationCloseTimer;
    @JsonProperty("user_defined")
    UserDefined userDefined;
}
