package com.ocado.pandateam.newrelic.sync.configuration.channel;

import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannel;
import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannelConfiguration;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static java.lang.String.format;

public final class ChannelUtils {
    private ChannelUtils() {
    }

    public static boolean sameInstance(AlertsChannel alertsChannel1, AlertsChannel alertsChannel2) {
        return StringUtils.equals(alertsChannel1.getName(), alertsChannel2.getName())
            && StringUtils.equals(alertsChannel1.getType(), alertsChannel2.getType());
    }

    public static boolean same(AlertsChannel alertsChannel1, AlertsChannel alertsChannel2) {
        AlertsChannelConfiguration alertsChannelConfiguration1 = alertsChannel1.getConfiguration();
        AlertsChannelConfiguration alertsChannelConfiguration2 = alertsChannel2.getConfiguration();
        return sameInstance(alertsChannel1, alertsChannel2)
            && StringUtils.equals(alertsChannelConfiguration1.getUserId(), alertsChannelConfiguration2.getUserId())
            && StringUtils.equals(alertsChannelConfiguration1.getChannel(), alertsChannelConfiguration2.getChannel())
            && StringUtils.equals(alertsChannelConfiguration1.getUrl(), alertsChannelConfiguration2.getUrl())
            && alertsChannelConfiguration1.getIncludeJsonAttachment() == alertsChannelConfiguration2.getIncludeJsonAttachment()
            && StringUtils.equals(alertsChannelConfiguration1.getRecipients(), alertsChannelConfiguration2.getRecipients())
            && StringUtils.equals(alertsChannelConfiguration1.getPayloadType(), alertsChannelConfiguration2.getPayloadType())
            && StringUtils.equals(alertsChannelConfiguration1.getPayload(), alertsChannelConfiguration2.getPayload())
            && headersEquals(alertsChannelConfiguration1.getHeaders(), alertsChannelConfiguration2.getHeaders())
            && StringUtils.equals(alertsChannelConfiguration1.getBaseUrl(), alertsChannelConfiguration2.getBaseUrl())
            && StringUtils.equals(alertsChannelConfiguration1.getAuthUsername(), alertsChannelConfiguration2.getAuthUsername())
            && StringUtils.equals(alertsChannelConfiguration1.getAuthPassword(), alertsChannelConfiguration2.getAuthPassword());
    }

    private static boolean headersEquals(Map<String, String> headers1, Map<String, String> headers2) {
        if (headers1 == null && headers2 == null) {
            return true;
        } else if (headers1 != null && headers2 != null) {
            return headers1.equals(headers2);
        }
        return false;
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
