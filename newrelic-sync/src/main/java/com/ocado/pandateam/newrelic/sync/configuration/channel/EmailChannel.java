package com.ocado.pandateam.newrelic.sync.configuration.channel;

import com.ocado.pandateam.newrelic.sync.configuration.channel.internal.EmailChannelTypeSupport;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Email channel configuration.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #channelName}</li>
 *     <li>{@link #emailAddress}</li>
 *     <li>{@link #includeJsonAttachment} (optional)</li>
 * </ul>
 */
@Builder
@Getter
public class EmailChannel implements Channel {
    private final ChannelType type = ChannelType.EMAIL;
    /**
     * Name of your alerts channel
     */
    @NonNull
    private String channelName;
    /**
     * Email address to which alert event should be sent
     */
    @NonNull
    private String emailAddress;
    /**
     * If json attachment should be included in alert event email. Default value is false
     */
    private Boolean includeJsonAttachment;

    private final ChannelTypeSupport channelTypeSupport = new EmailChannelTypeSupport(this);

    @Override
    public ChannelTypeSupport getChannelTypeSupport() {
        return channelTypeSupport;
    }
}
