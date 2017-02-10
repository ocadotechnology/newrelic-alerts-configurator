package com.ocadotechnology.newrelic.alertconfiguploader.configuration.channel.internal;

import com.ocadotechnology.newrelic.alertconfiguploader.configuration.channel.Channel;
import com.ocadotechnology.newrelic.alertconfiguploader.configuration.channel.ChannelTypeSupport;
import com.ocadotechnology.newrelic.alertconfiguploader.configuration.channel.PagerDutyChannel;
import com.ocadotechnology.newrelic.api.NewRelicApi;
import com.ocadotechnology.newrelic.api.model.channels.AlertsChannelConfiguration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PagerDutyChannelTypeSupport implements ChannelTypeSupport {
    private Channel channel;

    @Override
    public AlertsChannelConfiguration generateAlertsChannelConfiguration(NewRelicApi api) {
        PagerDutyChannel pagerDutyChannel = (PagerDutyChannel) channel;
        return AlertsChannelConfiguration.builder()
                .serviceKey(pagerDutyChannel.getServiceKey())
                .build();
    }
}
