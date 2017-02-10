package com.ocado.newrelic.alertconfiguploader.configuration.channel.internal;

import com.ocado.newrelic.alertconfiguploader.configuration.channel.Channel;
import com.ocado.newrelic.alertconfiguploader.configuration.channel.ChannelTypeSupport;
import com.ocado.newrelic.alertconfiguploader.configuration.channel.PagerDutyChannel;
import com.ocado.newrelic.api.NewRelicApi;
import com.ocado.newrelic.api.model.channels.AlertsChannelConfiguration;
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
