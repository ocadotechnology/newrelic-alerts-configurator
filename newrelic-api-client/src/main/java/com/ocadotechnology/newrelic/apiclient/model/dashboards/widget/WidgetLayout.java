package com.ocadotechnology.newrelic.apiclient.model.dashboards.widget;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class WidgetLayout {
    @JsonProperty
    Integer width;
    @JsonProperty
    Integer height;
    @JsonProperty
    Integer row;
    @JsonProperty
    Integer column;
}
