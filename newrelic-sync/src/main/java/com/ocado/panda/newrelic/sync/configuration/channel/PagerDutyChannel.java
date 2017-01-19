package com.ocado.panda.newrelic.sync.configuration.channel;

import com.ocado.panda.newrelic.sync.configuration.channel.internal.PagerDutyChannelTypeSupport;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class PagerDutyChannel implements Channel {
    private final ChannelType type = ChannelType.PAGERDUTY;
    /**
     * Name of your alerts channel
     */
    @NonNull
    private String channelName;
    /**
     * PagerDuty service key
     */
    @NonNull
    private String serviceKey;

    private final ChannelTypeSupport channelTypeSupport = new PagerDutyChannelTypeSupport(this);

    @Override
    public ChannelTypeSupport getChannelTypeSupport() {
        return channelTypeSupport;
    }
}
