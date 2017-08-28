package com.ocadotechnology.newrelic.apiclient.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.apiclient.model.channels.AlertsChannel;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class AlertsChannelList extends ObjectList<AlertsChannel, AlertsChannelList> {
    @JsonCreator
    public AlertsChannelList(@JsonProperty("channels") List<AlertsChannel> items) {
        super(items, AlertsChannelList::new);
    }
}
