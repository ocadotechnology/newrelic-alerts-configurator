package com.ocadotechnology.newrelic.apiclient.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.apiclient.model.policies.AlertsPolicyChannels;
import lombok.Value;

@Value
public class AlertsPolicyChannelsWrapper {
    @JsonProperty("policy")
    AlertsPolicyChannels policyChannels;
}
