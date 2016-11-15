package com.ocado.pandateam.newrelic.sync.channel;

import com.ocado.pandateam.newrelic.api.model.AlertChannel;
import com.ocado.pandateam.newrelic.api.model.AlertChannelConfiguration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmailChannel implements Channel {
    private String channelName;
    private String emailAddress;
    private Boolean includeJsonAttachment;

    public String getIncludeJsonAttachment() {
        return includeJsonAttachment == null ? null : String.valueOf(includeJsonAttachment);
    }

    @Override
    public AlertChannel getAsAlertChannel() {
        AlertChannelConfiguration config = AlertChannelConfiguration.builder()
                .recipients(emailAddress).includeJsonAttachment(getIncludeJsonAttachment()).build();
        return AlertChannel.builder().name(channelName).type("email").configuration(config).build();
    }
}
