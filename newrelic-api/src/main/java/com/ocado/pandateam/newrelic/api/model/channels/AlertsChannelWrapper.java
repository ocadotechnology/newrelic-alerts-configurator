package com.ocado.pandateam.newrelic.api.model.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class AlertsChannelWrapper {
    @JsonProperty
    AlertsChannel channel;
}
