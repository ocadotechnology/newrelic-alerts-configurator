package com.ocadotechnology.newrelic.apiclient.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.apiclient.model.policies.AlertsPolicy;
import lombok.Value;

@Value
public class AlertsPolicyWrapper {
    @JsonProperty
    AlertsPolicy policy;
}
