package com.ocado.panda.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.panda.newrelic.api.model.conditions.AlertsCondition;
import lombok.Value;

@Value
public class AlertsConditionWrapper {
    @JsonProperty
    AlertsCondition condition;
}
