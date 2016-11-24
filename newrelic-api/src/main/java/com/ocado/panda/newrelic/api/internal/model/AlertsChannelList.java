package com.ocado.panda.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.panda.newrelic.api.model.ObjectList;
import com.ocado.panda.newrelic.api.model.channels.AlertsChannel;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class AlertsChannelList extends ObjectList<AlertsChannel> {
    @JsonProperty("channels")
    List<AlertsChannel> list;

    @Override
    public AlertsChannelList merge(ObjectList<AlertsChannel> list) {
        return new AlertsChannelList(mergeList(list));
    }
}
