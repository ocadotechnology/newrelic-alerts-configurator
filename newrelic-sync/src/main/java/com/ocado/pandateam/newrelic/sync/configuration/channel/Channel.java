package com.ocado.pandateam.newrelic.sync.configuration.channel;

import com.ocado.pandateam.newrelic.api.model.channels.AlertChannel;

public interface Channel {
    AlertChannel getAsAlertChannel();
}
