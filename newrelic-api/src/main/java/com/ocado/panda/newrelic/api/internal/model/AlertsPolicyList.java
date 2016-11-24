package com.ocado.panda.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.panda.newrelic.api.model.ObjectList;
import com.ocado.panda.newrelic.api.model.policies.AlertsPolicy;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class AlertsPolicyList extends ObjectList<AlertsPolicy> {
    @JsonProperty("policies")
    List<AlertsPolicy> list;

    @Override
    public AlertsPolicyList merge(ObjectList<AlertsPolicy> list) {
        return new AlertsPolicyList(mergeList(list));
    }
}
