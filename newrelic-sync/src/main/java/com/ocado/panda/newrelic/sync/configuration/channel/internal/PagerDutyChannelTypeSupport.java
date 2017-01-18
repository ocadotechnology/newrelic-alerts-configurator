package com.ocado.panda.newrelic.sync.configuration.channel.internal;

import com.ocado.panda.newrelic.api.model.channels.AlertsChannelConfiguration;
import com.ocado.panda.newrelic.sync.configuration.channel.Channel;
import com.ocado.panda.newrelic.sync.configuration.channel.ChannelTypeSupport;
import com.ocado.panda.newrelic.sync.configuration.channel.PagerDutyChannel;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PagerDutyChannelTypeSupport implements ChannelTypeSupport {
    private Channel channel;

    @Override
    public AlertsChannelConfiguration generateAlertsChannelConfiguration() {
        PagerDutyChannel pagerDutyChannel = (PagerDutyChannel) channel;
        return AlertsChannelConfiguration.builder()
                .serviceKey(pagerDutyChannel.getServiceKey())
                .build();
    }
}
