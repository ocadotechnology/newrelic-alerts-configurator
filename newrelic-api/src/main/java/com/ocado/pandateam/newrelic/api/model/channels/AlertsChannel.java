package com.ocado.pandateam.newrelic.api.model.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class AlertsChannel {
    @JsonProperty
    Integer id;
    @JsonProperty
    String name;
    @JsonProperty
    String type;
    @JsonProperty
    AlertsChannelConfiguration configuration;
    @JsonProperty
    AlertsChannelLinks links;
}
