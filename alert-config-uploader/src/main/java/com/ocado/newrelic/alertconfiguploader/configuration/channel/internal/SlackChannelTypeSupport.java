package com.ocado.newrelic.alertconfiguploader.configuration.channel.internal;

import com.ocado.newrelic.alertconfiguploader.configuration.channel.Channel;
import com.ocado.newrelic.alertconfiguploader.configuration.channel.ChannelTypeSupport;
import com.ocado.newrelic.alertconfiguploader.configuration.channel.SlackChannel;
import com.ocado.newrelic.api.NewRelicApi;
import com.ocado.newrelic.api.model.channels.AlertsChannelConfiguration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SlackChannelTypeSupport implements ChannelTypeSupport {
    private Channel channel;

    @Override
    public AlertsChannelConfiguration generateAlertsChannelConfiguration(NewRelicApi api) {
        SlackChannel slackChannel = (SlackChannel) channel;
        return AlertsChannelConfiguration.builder()
            .url(slackChannel.getSlackUrl())
            .channel(slackChannel.getTeamChannel())
            .build();
    }
}
