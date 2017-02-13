package com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.internal;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.Channel;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.ChannelTypeSupport;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.SlackChannel;
import com.ocadotechnology.newrelic.api.NewRelicApi;
import com.ocadotechnology.newrelic.api.model.channels.AlertsChannelConfiguration;
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
