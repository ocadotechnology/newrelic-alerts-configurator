package com.ocadotechnology.newrelic.apiclient.model.applications;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class EndUserSummary {
    @JsonProperty("response_time")
    Float responseTime;
    @JsonProperty
    Float throughput;
    @JsonProperty("apdex_target")
    Float apdexTarget;
    @JsonProperty("apdex_score")
    Float apdexScore;
}
