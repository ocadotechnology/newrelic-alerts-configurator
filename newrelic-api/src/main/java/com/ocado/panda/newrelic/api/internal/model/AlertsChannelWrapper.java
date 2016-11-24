package com.ocado.panda.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.panda.newrelic.api.model.channels.AlertsChannel;
import lombok.Value;

@Value
public class AlertsChannelWrapper {
    @JsonProperty
    AlertsChannel channel;
}
