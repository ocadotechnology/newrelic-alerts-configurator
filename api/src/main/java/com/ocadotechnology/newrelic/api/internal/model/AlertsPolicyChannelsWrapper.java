package com.ocadotechnology.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.api.model.policies.AlertsPolicyChannels;
import lombok.Value;

@Value
public class AlertsPolicyChannelsWrapper {
    @JsonProperty("policy")
    AlertsPolicyChannels policyChannels;
}
