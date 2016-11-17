package com.ocado.pandateam.newrelic.sync.configuration.channel;

import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannel;
import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannelConfiguration;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;
import org.apache.commons.lang3.StringUtils;

import static java.lang.String.format;

public final class ChannelUtils {
    private ChannelUtils() {
    }

    public static boolean sameInstance(AlertsChannel alertChannel1, AlertsChannel alertChannel2) {
        return StringUtils.equals(alertChannel1.getName(), alertChannel2.getName())
            && StringUtils.equals(alertChannel1.getType(), alertChannel2.getType());
    }

    public static boolean same(AlertsChannel alertChannel1, AlertsChannel alertChannel2) {
        return sameInstance(alertChannel1, alertChannel2)
            && alertChannel1.getConfiguration().equals(alertChannel2.getConfiguration());
    }

    public static AlertsChannelConfiguration generateAlertsChannelConfiguration(Channel channel) {
        switch (channel.getType()) {
            case EMAIL:
                EmailChannel emailChannel = (EmailChannel) channel;
                return AlertsChannelConfiguration.builder()
                    .recipients(emailChannel.getEmailAddress())
                    .includeJsonAttachment(emailChannel.getIncludeJsonAttachment())
                    .build();
            case SLACK:
                SlackChannel slackChannel = (SlackChannel) channel;
                return AlertsChannelConfiguration.builder()
                    .url(slackChannel.getSlackUrl())
                    .channel(slackChannel.getSlackChannel())
                    .build();
            default:
                throw new NewRelicSyncException(
                    format("Could not create configuration for channel %s", channel.getChannelName()));
        }
    }

}
