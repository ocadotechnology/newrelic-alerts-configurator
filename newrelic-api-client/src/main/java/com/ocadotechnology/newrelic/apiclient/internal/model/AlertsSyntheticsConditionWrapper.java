package com.ocadotechnology.newrelic.apiclient.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.apiclient.model.conditions.synthetics.AlertsSyntheticsCondition;

import lombok.Value;

@Value
public class AlertsSyntheticsConditionWrapper {
    @JsonProperty("synthetics_condition")
    AlertsSyntheticsCondition syntheticsCondition;
}
