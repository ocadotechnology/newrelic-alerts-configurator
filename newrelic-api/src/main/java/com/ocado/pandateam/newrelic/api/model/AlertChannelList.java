package com.ocado.pandateam.newrelic.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class AlertChannelList implements ObjectList<AlertChannel> {
    @JsonProperty("channels")
    List<AlertChannel> list;
}
