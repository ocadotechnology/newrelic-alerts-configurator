package com.ocado.pandateam.newrelic.sync.configuration.channel;

import com.ocado.pandateam.newrelic.sync.configuration.channel.internal.EmailChannelTypeSupport;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class EmailChannel implements Channel {
    private final ChannelType type = ChannelType.EMAIL;
    @NonNull
    private String channelName;
    @NonNull
    private String emailAddress;
    private Boolean includeJsonAttachment;

    private final ChannelTypeSupport channelTypeSupport = new EmailChannelTypeSupport(this);

    @Override
    public ChannelTypeSupport getChannelTypeSupport() {
        return channelTypeSupport;
    }
}
