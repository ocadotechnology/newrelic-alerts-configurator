package com.ocado.pandateam.newrelic.sync.configuration.channel;

import com.ocado.pandateam.newrelic.api.model.channels.AlertChannelConfiguration;

public interface Channel {
    String getChannelName();
    String getType();
    AlertChannelConfiguration getAlertChannelConfiguration();
}
