package com.ocado.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.newrelic.api.model.conditions.AlertsCondition;
import lombok.Value;

@Value
public class AlertsConditionWrapper {
    @JsonProperty
    AlertsCondition condition;
}
