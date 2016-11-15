package com.ocado.pandateam.newrelic.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class ExternalServiceConditionList extends ObjectList<ExternalServiceCondition> {
    @JsonProperty("conditions")
    List<ExternalServiceCondition> list;
}
