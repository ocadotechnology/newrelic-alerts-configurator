package com.ocado.pandateam.newrelic.api.model.conditions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class AlertCondition {
    Integer id;
    String type;
    String name;
    boolean enabled;
    Integer[] entities;
    String metric;
    @JsonProperty("condition_scope")
    String conditionScope;
    @JsonProperty("runbook_url")
    String runbookUrl;
    Terms[] terms;
    UserDefined userDefined;
}
