package com.ocado.pandateam.newrelic.sync.configuration.channel;

import com.ocado.pandateam.newrelic.sync.configuration.channel.internal.SlackChannelTypeSupport;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class SlackChannel implements Channel {
    private final ChannelType type = ChannelType.SLACK;
    @NonNull
    private String channelName;
    @NonNull
    private String slackUrl;
    private String slackChannel;

    private final ChannelTypeSupport channelTypeSupport = new SlackChannelTypeSupport(this);

    @Override
    public ChannelTypeSupport getChannelTypeSupport() {
        return channelTypeSupport;
    }
}
