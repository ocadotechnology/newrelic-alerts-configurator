package com.ocado.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.newrelic.api.model.channels.AlertsChannel;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class AlertsChannelList extends ObjectList<AlertsChannel, AlertsChannelList> {
    @JsonCreator
    public AlertsChannelList(@JsonProperty("channels") List<AlertsChannel> items) {
        super(items, AlertsChannelList::new);
    }
}
