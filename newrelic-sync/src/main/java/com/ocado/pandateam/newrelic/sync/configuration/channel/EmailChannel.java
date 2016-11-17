package com.ocado.pandateam.newrelic.sync.configuration.channel;

import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannelConfiguration;
import lombok.Builder;
import lombok.NonNull;

@Builder
public class EmailChannel implements Channel {
    @NonNull
    private String channelName;
    @NonNull
    private String emailAddress;
    private Boolean includeJsonAttachment;

    public String getIncludeJsonAttachment() {
        return includeJsonAttachment == null ? null : String.valueOf(includeJsonAttachment);
    }

    @Override
    public String getChannelName() {
        return channelName;
    }

    @Override
    public String getType() {
        return ChannelType.EMAIL.name().toLowerCase();
    }

    @Override
    public AlertsChannelConfiguration getAlertChannelConfiguration() {
        return AlertsChannelConfiguration.builder()
                .recipients(emailAddress)
                .includeJsonAttachment(includeJsonAttachment)
                .build();
    }
}
