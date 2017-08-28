package com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.internal;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.Channel;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.ChannelTypeSupport;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.EmailChannel;
import com.ocadotechnology.newrelic.apiclient.NewRelicApi;
import com.ocadotechnology.newrelic.apiclient.model.channels.AlertsChannelConfiguration;
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
