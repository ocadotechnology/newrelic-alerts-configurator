package com.ocado.pandateam.newrelic.api.model.policies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class AlertsPolicyChannelsWrapper {
    @JsonProperty("policy")
    AlertsPolicyChannels policyChannels;
}
