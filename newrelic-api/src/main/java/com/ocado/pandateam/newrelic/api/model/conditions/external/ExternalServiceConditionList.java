package com.ocado.pandateam.newrelic.api.model.conditions.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.pandateam.newrelic.api.model.ObjectList;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class ExternalServiceConditionList extends ObjectList<ExternalServiceCondition> {
    @JsonProperty("external_service_conditions")
    List<ExternalServiceCondition> list;
}
