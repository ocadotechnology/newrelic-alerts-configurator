package com.ocadotechnology.newrelic.apiclient.model.conditions.nrql;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Signal {
    @JsonProperty("aggregation_window")
    String aggregationWindow;
    @JsonProperty("evaluation_offset")
    String evaluationOffset;
    @JsonProperty("fill_option")
    String fillOption;
    @JsonProperty("fill_value")
    String fillValue;
}
