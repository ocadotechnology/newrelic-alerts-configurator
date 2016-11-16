package com.ocado.pandateam.newrelic.sync.configuration.channel;

import com.ocado.pandateam.newrelic.api.model.channels.AlertChannel;
import com.ocado.pandateam.newrelic.api.model.channels.AlertChannelConfiguration;
import lombok.Builder;
import lombok.NonNull;

@Builder
public class EmailChannel implements Channel {
    @NonNull
    private String channelName;
    @NonNull
    private String emailAddress;
    private boolean includeJsonAttachment;

    public String getIncludeJsonAttachment() {
        return String.valueOf(includeJsonAttachment);
    }

    @Override
    public AlertChannel getAsAlertChannel() {
        AlertChannelConfiguration config = AlertChannelConfiguration.builder()
                .recipients(emailAddress).includeJsonAttachment(getIncludeJsonAttachment()).build();
        return AlertChannel.builder().name(channelName).type("email").configuration(config).build();
    }
}
