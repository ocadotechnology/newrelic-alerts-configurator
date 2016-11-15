package com.ocado.pandateam.newrelic.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Terms {
    String duration;
    String operator;
    String priority;
    String threshold;
    @JsonProperty("time_function")
    String timeFunction;
}
