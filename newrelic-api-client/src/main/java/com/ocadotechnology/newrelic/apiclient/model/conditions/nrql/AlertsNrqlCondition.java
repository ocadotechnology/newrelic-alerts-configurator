package com.ocadotechnology.newrelic.apiclient.model.conditions.nrql;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.apiclient.model.PolicyItem;
import com.ocadotechnology.newrelic.apiclient.model.conditions.Expiration;
import com.ocadotechnology.newrelic.apiclient.model.conditions.Signal;
import com.ocadotechnology.newrelic.apiclient.model.conditions.Terms;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * See <a href="https://rpm.newrelic.com/api/explore/alerts_nrql_conditions/list">Doc</a>
 */
@Value
@Builder
@AllArgsConstructor
@NonFinal
public class AlertsNrqlCondition implements PolicyItem {
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
    @JsonProperty
    Signal signal;
    @JsonProperty
    Expiration expiration;
}
