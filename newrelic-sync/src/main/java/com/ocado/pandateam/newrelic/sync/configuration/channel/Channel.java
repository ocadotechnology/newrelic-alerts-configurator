package com.ocado.pandateam.newrelic.sync.configuration.channel;

public interface Channel {
    ChannelType getType();
    String getTypeString();
    String getChannelName();
}
