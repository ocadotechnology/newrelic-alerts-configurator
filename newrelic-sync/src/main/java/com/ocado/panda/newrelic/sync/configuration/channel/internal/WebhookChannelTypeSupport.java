package com.ocado.panda.newrelic.sync.configuration.channel.internal;

import com.ocado.panda.newrelic.api.NewRelicApi;
import com.ocado.panda.newrelic.api.model.channels.AlertsChannelConfiguration;
import com.ocado.panda.newrelic.sync.configuration.channel.Channel;
import com.ocado.panda.newrelic.sync.configuration.channel.ChannelTypeSupport;
import com.ocado.panda.newrelic.sync.configuration.channel.WebhookChannel;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WebhookChannelTypeSupport implements ChannelTypeSupport {
    private Channel channel;

    @Override
    public AlertsChannelConfiguration generateAlertsChannelConfiguration(NewRelicApi api) {
        WebhookChannel webhookChannel = (WebhookChannel) channel;
        return AlertsChannelConfiguration.builder()
                .baseUrl(webhookChannel.getBaseUrl())
                .authUsername(webhookChannel.getAuthUsername())
                .authPassword(webhookChannel.getAuthPassword())
                .build();
    }
}
