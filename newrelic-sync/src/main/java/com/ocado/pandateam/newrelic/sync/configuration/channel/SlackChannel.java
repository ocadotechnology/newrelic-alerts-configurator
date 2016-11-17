package com.ocado.pandateam.newrelic.sync.configuration.channel;

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

    @Override
    public String getTypeString() {
        return type.name().toLowerCase();
    }
}
