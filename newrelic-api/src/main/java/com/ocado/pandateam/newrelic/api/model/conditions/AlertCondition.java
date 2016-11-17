package com.ocado.pandateam.newrelic.api.model.conditions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Collection;

@Value
@Builder
@AllArgsConstructor
public class AlertCondition {
    @JsonProperty
    Integer id;
    @JsonProperty
    String type;
    @JsonProperty
    String name;
    @JsonProperty
    Boolean enabled;
    @JsonProperty
    Collection<Integer> entities;
    @JsonProperty
    String metric;
    @JsonProperty("condition_scope")
    String conditionScope;
    @JsonProperty("runbook_url")
    String runbookUrl;
    @JsonProperty
    Collection<Terms> terms;
    @JsonProperty
    UserDefined userDefined;
}
