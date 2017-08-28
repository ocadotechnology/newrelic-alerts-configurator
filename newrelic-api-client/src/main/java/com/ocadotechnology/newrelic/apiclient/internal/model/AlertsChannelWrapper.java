package com.ocadotechnology.newrelic.apiclient.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.apiclient.model.channels.AlertsChannel;
import lombok.Value;

@Value
public class AlertsChannelWrapper {
    @JsonProperty
    AlertsChannel channel;
}
