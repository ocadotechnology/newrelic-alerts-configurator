package com.ocado.panda.newrelic.sync.configuration.channel.internal;

import com.ocado.newrelic.api.NewRelicApi;
import com.ocado.newrelic.api.model.channels.AlertsChannelConfiguration;
import com.ocado.panda.newrelic.sync.configuration.channel.Channel;
import com.ocado.panda.newrelic.sync.configuration.channel.ChannelTypeSupport;
import com.ocado.panda.newrelic.sync.configuration.channel.SlackChannel;
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
