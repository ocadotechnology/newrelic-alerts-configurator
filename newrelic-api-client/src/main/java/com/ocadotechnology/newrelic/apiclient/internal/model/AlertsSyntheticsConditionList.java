package com.ocadotechnology.newrelic.apiclient.internal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.apiclient.model.conditions.synthetics.AlertsSyntheticsCondition;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class AlertsSyntheticsConditionList extends ObjectList<AlertsSyntheticsCondition, AlertsSyntheticsConditionList> {
    @JsonCreator
    public AlertsSyntheticsConditionList(@JsonProperty("synthetics_conditions") List<AlertsSyntheticsCondition> items) {
        super(items, AlertsSyntheticsConditionList::new);
    }
}
