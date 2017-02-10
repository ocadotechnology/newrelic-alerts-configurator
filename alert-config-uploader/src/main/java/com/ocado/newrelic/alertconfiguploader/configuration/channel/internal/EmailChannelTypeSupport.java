package com.ocado.newrelic.alertconfiguploader.configuration.channel.internal;

import com.ocado.newrelic.alertconfiguploader.configuration.channel.Channel;
import com.ocado.newrelic.alertconfiguploader.configuration.channel.ChannelTypeSupport;
import com.ocado.newrelic.alertconfiguploader.configuration.channel.EmailChannel;
import com.ocado.newrelic.api.NewRelicApi;
import com.ocado.newrelic.api.model.channels.AlertsChannelConfiguration;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;

@AllArgsConstructor
public class EmailChannelTypeSupport implements ChannelTypeSupport {
    private Channel channel;

    @Override
    public AlertsChannelConfiguration generateAlertsChannelConfiguration(NewRelicApi api) {
        EmailChannel emailChannel = (EmailChannel) channel;
        AlertsChannelConfiguration.AlertsChannelConfigurationBuilder builder = AlertsChannelConfiguration.builder();
        builder.recipients(emailChannel.getEmailAddress());
        if (BooleanUtils.isTrue(emailChannel.getIncludeJsonAttachment())) {
            builder.includeJsonAttachment(emailChannel.getIncludeJsonAttachment());
        }
        return builder.build();
    }
}
