package com.ocado.panda.newrelic.api.model.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * See <a href="https://rpm.newrelic.com/api/explore/alerts_channels/list">Doc</a>
 */
@Value
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = {"name", "type", "configuration"})
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
