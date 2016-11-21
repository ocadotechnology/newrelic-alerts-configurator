package com.ocado.pandateam.newrelic.sync.configuration.channel.internal;

import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannelConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.channel.Channel;
import com.ocado.pandateam.newrelic.sync.configuration.channel.ChannelTypeSupport;
import com.ocado.pandateam.newrelic.sync.configuration.channel.EmailChannel;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmailChannelTypeSupport implements ChannelTypeSupport {
    private Channel channel;

    @Override
    public AlertsChannelConfiguration generateAlertsChannelConfiguration() {
        EmailChannel emailChannel = (EmailChannel) channel;
        return AlertsChannelConfiguration.builder()
            .recipients(emailChannel.getEmailAddress())
            .includeJsonAttachment(emailChannel.getIncludeJsonAttachment())
            .build();
    }
}
