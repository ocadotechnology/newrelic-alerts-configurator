package com.ocado.panda.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.panda.newrelic.api.model.policies.AlertsPolicyChannels;
import lombok.Value;

@Value
public class AlertsPolicyChannelsWrapper {
    @JsonProperty("policy")
    AlertsPolicyChannels policyChannels;
}
