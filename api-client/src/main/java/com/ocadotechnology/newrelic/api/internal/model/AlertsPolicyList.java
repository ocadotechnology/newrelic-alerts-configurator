package com.ocadotechnology.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.api.model.policies.AlertsPolicy;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class AlertsPolicyList extends ObjectList<AlertsPolicy, AlertsPolicyList> {
    @JsonCreator
    public AlertsPolicyList(@JsonProperty("policies") List<AlertsPolicy> items) {
        super(items, AlertsPolicyList::new);
    }
}
