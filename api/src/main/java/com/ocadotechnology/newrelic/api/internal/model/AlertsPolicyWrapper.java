package com.ocadotechnology.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.api.model.policies.AlertsPolicy;
import lombok.Value;

@Value
public class AlertsPolicyWrapper {
    @JsonProperty
    AlertsPolicy policy;
}
