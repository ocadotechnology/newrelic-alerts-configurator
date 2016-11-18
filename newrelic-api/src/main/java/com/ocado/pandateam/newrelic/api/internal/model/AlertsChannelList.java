package com.ocado.pandateam.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.pandateam.newrelic.api.model.ObjectList;
import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannel;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class AlertsChannelList extends ObjectList<AlertsChannel> {
    @JsonProperty("channels")
    List<AlertsChannel> list;
}
