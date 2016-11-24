package com.ocado.panda.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.panda.newrelic.api.model.ObjectList;
import com.ocado.panda.newrelic.api.model.conditions.external.AlertsExternalServiceCondition;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class AlertsExternalServiceConditionList extends ObjectList<AlertsExternalServiceCondition> {
    @JsonProperty("external_service_conditions")
    List<AlertsExternalServiceCondition> list;

    @Override
    public AlertsExternalServiceConditionList merge(ObjectList<AlertsExternalServiceCondition> list) {
        return new AlertsExternalServiceConditionList(mergeList(list));
    }
}
