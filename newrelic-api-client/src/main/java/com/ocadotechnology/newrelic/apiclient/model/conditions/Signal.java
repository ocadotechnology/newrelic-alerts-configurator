package com.ocadotechnology.newrelic.apiclient.model.conditions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Signal {
    @JsonProperty("aggregation_method")
    String aggregationMethod;
    @JsonProperty("aggregation_delay")
    String aggregationDelay;
    @JsonProperty("aggregation_timer")
    String aggregationTimer;
    @JsonProperty("aggregation_window")
    String aggregationWindow;
    @JsonProperty("fill_option")
    String fillOption;
    @JsonInclude
    @JsonProperty("fill_value")
    String fillValue;
}
