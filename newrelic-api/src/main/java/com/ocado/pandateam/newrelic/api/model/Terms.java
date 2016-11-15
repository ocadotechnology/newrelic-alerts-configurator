package com.ocado.pandateam.newrelic.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Terms {
    String duration;
    String operator;
    String priority;
    String threshold;
    @JsonProperty("time_function")
    String timeFunction;
}
