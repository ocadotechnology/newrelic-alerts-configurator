package com.ocado.panda.newrelic.sync.configuration.channel.internal;

import com.ocado.panda.newrelic.api.model.channels.AlertsChannelConfiguration;
import com.ocado.panda.newrelic.sync.configuration.channel.Channel;
import com.ocado.panda.newrelic.sync.configuration.channel.ChannelTypeSupport;
import com.ocado.panda.newrelic.sync.configuration.channel.EmailChannel;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;

@AllArgsConstructor
public class EmailChannelTypeSupport implements ChannelTypeSupport {
    private Channel channel;

    @Override
    public AlertsChannelConfiguration generateAlertsChannelConfiguration() {
        EmailChannel emailChannel = (EmailChannel) channel;
        AlertsChannelConfiguration.AlertsChannelConfigurationBuilder builder = AlertsChannelConfiguration.builder();
        builder.recipients(emailChannel.getEmailAddress());
        if (BooleanUtils.isTrue(emailChannel.getIncludeJsonAttachment())) {
            builder.includeJsonAttachment(emailChannel.getIncludeJsonAttachment());
        }
        return builder.build();
    }
}
