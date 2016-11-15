package com.ocado.pandateam.newrelic.sync.channel;

import com.ocado.pandateam.newrelic.api.model.AlertChannel;

public interface Channel {
    AlertChannel getAsAlertChannel();
}
