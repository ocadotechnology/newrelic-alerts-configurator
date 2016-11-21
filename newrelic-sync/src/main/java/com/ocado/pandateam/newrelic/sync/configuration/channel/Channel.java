package com.ocado.pandateam.newrelic.sync.configuration.channel;

public interface Channel {
    ChannelType getType();
    String getChannelName();
    ChannelTypeSupport getChannelTypeSupport();
    default String getTypeString() {
        return getType().name().toLowerCase();
    }
}
