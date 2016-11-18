package com.ocado.pandateam.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import lombok.Value;

@Value
public class AlertsPolicyWrapper {
    @JsonProperty
    AlertsPolicy policy;
}
