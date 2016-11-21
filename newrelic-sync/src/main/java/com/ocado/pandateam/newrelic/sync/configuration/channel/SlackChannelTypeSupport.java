package com.ocado.pandateam.newrelic.sync.configuration.channel;

import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannelConfiguration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SlackChannelTypeSupport implements ChannelTypeSupport {
    private Channel channel;

    @Override
    public AlertsChannelConfiguration generateAlertsChannelConfiguration() {
        SlackChannel slackChannel = (SlackChannel) channel;
        return AlertsChannelConfiguration.builder()
            .url(slackChannel.getSlackUrl())
            .channel(slackChannel.getSlackChannel())
            .build();
    }
}
