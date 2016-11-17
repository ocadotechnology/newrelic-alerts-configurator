package com.ocado.pandateam.newrelic.sync.configuration.channel;

import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannelConfiguration;

public interface Channel {
    String getChannelName();
    String getType();
    AlertsChannelConfiguration getAlertChannelConfiguration();
}
