package com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.internal.UserChannelTypeSupport;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * User channel configuration.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #userEmail}</li>
 * </ul>
 */
@Builder
@Getter
public class UserChannel implements Channel {
    private final ChannelType type = ChannelType.USER;
    /**
     * Email of NewRelic user
     */
    @NonNull
    private String userEmail;

    private final ChannelTypeSupport channelTypeSupport = new UserChannelTypeSupport(this);

    @Override
    public String getChannelName() {
        return "User " + userEmail;
    }

    @Override
    public ChannelTypeSupport getChannelTypeSupport() {
        return channelTypeSupport;
    }
}
