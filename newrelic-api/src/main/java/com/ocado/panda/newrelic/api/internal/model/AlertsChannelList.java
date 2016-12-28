package com.ocado.panda.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.panda.newrelic.api.model.ObjectList;
import com.ocado.panda.newrelic.api.model.channels.AlertsChannel;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class AlertsChannelList extends ObjectList<AlertsChannel, AlertsChannelList> {
    @JsonCreator
    public AlertsChannelList(@JsonProperty("channels") List<AlertsChannel> items) {
        super(items, AlertsChannelList::new);
    }
}
