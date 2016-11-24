package com.ocado.pandateam.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.pandateam.newrelic.api.model.ObjectList;
import com.ocado.pandateam.newrelic.api.model.conditions.AlertsCondition;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class AlertsConditionList extends ObjectList<AlertsCondition> {
    @JsonProperty("conditions")
    List<AlertsCondition> list;

    @Override
    public AlertsConditionList merge(ObjectList<AlertsCondition> list) {
        return new AlertsConditionList(mergeList(list));
    }
}
