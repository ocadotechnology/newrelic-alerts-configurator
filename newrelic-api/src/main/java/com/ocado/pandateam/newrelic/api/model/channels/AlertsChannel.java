package com.ocado.pandateam.newrelic.api.model.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * See <a href="https://rpm.newrelic.com/api/explore/alerts_channels/list">Doc</a>
 */
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
