package com.ocado.pandateam.newrelic.api.model.conditions.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class AlertsExternalServiceConditionWrapper {
    @JsonProperty("external_service_condition")
    AlertsExternalServiceCondition externalServiceCondition;
}
