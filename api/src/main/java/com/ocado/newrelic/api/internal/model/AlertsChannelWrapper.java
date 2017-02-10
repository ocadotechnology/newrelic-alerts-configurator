package com.ocado.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.newrelic.api.model.channels.AlertsChannel;
import lombok.Value;

@Value
public class AlertsChannelWrapper {
    @JsonProperty
    AlertsChannel channel;
}
