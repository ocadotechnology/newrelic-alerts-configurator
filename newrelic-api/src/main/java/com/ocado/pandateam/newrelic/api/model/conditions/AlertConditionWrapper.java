package com.ocado.pandateam.newrelic.api.model.conditions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class AlertConditionWrapper {
    @JsonProperty
    AlertCondition condition;
}
