package com.ocado.pandateam.newrelic.api.model.conditions.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.pandateam.newrelic.api.model.ObjectList;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class AlertsExternalServiceConditionList extends ObjectList<AlertsExternalServiceCondition> {
    @JsonProperty("external_service_conditions")
    List<AlertsExternalServiceCondition> list;
}
