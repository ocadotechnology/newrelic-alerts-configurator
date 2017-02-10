package com.ocadotechnology.newrelic.alertconfiguploader.configuration.channel.internal;

import com.ocadotechnology.newrelic.alertconfiguploader.configuration.channel.Channel;
import com.ocadotechnology.newrelic.alertconfiguploader.configuration.channel.ChannelTypeSupport;
import com.ocadotechnology.newrelic.alertconfiguploader.configuration.channel.WebhookChannel;
import com.ocadotechnology.newrelic.api.NewRelicApi;
import com.ocadotechnology.newrelic.api.model.channels.AlertsChannelConfiguration;
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
