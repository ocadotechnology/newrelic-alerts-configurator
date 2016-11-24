package com.ocado.panda.newrelic.sync.configuration.channel;

import com.ocado.panda.newrelic.api.model.channels.AlertsChannelConfiguration;

public interface ChannelTypeSupport {
    AlertsChannelConfiguration generateAlertsChannelConfiguration();
}
