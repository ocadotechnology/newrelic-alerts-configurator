package com.ocado.pandateam.newrelic.sync.configuration.channel;

import com.ocado.pandateam.newrelic.sync.configuration.channel.internal.SlackChannelTypeSupport;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Slack channel configuration.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #channelName}</li>
 *     <li>{@link #slackUrl}</li>
 *     <li>{@link #teamChannel} (optional)</li>
 * </ul>
 */
@Builder
@Getter
public class SlackChannel implements Channel {
    private final ChannelType type = ChannelType.SLACK;
    /**
     * Name of your alerts channel
     */
    @NonNull
    private String channelName;
    /**
     * Slack url to your channel
     */
    @NonNull
    private String slackUrl;
    /**
     * Name of your team channel
     */
    private String teamChannel;

    private final ChannelTypeSupport channelTypeSupport = new SlackChannelTypeSupport(this);

    @Override
    public ChannelTypeSupport getChannelTypeSupport() {
        return channelTypeSupport;
    }
}
