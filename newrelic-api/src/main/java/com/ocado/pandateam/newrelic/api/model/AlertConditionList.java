package com.ocado.pandateam.newrelic.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class AlertConditionList extends ObjectList<AlertCondition> {
    @JsonProperty("conditions")
    List<AlertCondition> list;
}
