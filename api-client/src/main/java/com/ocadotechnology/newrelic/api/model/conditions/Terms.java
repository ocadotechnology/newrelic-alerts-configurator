package com.ocadotechnology.newrelic.api.model.conditions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Terms {
    @JsonProperty
    String duration;
    @JsonProperty
    String operator;
    @JsonProperty
    String priority;
    @JsonProperty
    String threshold;
    @JsonProperty("time_function")
    String timeFunction;
}
