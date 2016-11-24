package com.ocado.panda.newrelic.api.model.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class AlertsChannelLinks {
    @JsonProperty("policy_ids")
    List<Integer> policyIds;
}
