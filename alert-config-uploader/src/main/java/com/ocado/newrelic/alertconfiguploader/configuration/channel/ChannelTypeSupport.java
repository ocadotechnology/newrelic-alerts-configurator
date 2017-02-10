package com.ocado.newrelic.alertconfiguploader.configuration.channel;

import com.ocado.newrelic.api.NewRelicApi;
import com.ocado.newrelic.api.model.channels.AlertsChannelConfiguration;

public interface ChannelTypeSupport {
    AlertsChannelConfiguration generateAlertsChannelConfiguration(NewRelicApi api);
}
