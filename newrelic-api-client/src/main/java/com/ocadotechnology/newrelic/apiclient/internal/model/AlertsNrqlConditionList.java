package com.ocadotechnology.newrelic.apiclient.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.apiclient.model.conditions.AlertsNrqlCondition;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class AlertsNrqlConditionList extends ObjectList<AlertsNrqlCondition, AlertsNrqlConditionList> {
    @JsonCreator
    public AlertsNrqlConditionList(@JsonProperty("nrql_conditions") List<AlertsNrqlCondition> items) {
        super(items, AlertsNrqlConditionList::new);
    }
}
