package com.ocado.pandateam.newrelic.api.model.policies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.pandateam.newrelic.api.model.ObjectList;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class AlertsPolicyList extends ObjectList<AlertsPolicy> {
    @JsonProperty("policies")
    List<AlertsPolicy> list;
}
