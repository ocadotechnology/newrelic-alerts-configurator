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
    Integer id;
    String type;
    String name;
    boolean enabled;
    Collection<Integer> entities;
    String metric;
    @JsonProperty("condition_scope")
    String conditionScope;
    @JsonProperty("runbook_url")
    String runbookUrl;
    Collection<Terms> terms;
    UserDefined userDefined;
}
