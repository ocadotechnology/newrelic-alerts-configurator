package com.ocadotechnology.newrelic.alertconfiguploader.configuration.channel;

import com.ocadotechnology.newrelic.alertconfiguploader.configuration.channel.internal.WebhookChannelTypeSupport;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class WebhookChannel implements Channel {
    private final ChannelType type = ChannelType.WEBHOOK;
    /**
     * Name of your alerts channel
     */
    @NonNull
    private String channelName;
    /**
     * URL of the webhook
     */
    @NonNull
    private String baseUrl;
    /**
     * User name for Basic Auth
     */
    private String authUsername;
    /**
     * Password name for Basic Auth
     */
    private String authPassword;

    private final ChannelTypeSupport channelTypeSupport = new WebhookChannelTypeSupport(this);

    @Override
    public ChannelTypeSupport getChannelTypeSupport() {
        return channelTypeSupport;
    }
}
