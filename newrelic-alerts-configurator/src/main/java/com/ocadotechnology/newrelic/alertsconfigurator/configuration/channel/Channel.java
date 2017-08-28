package com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel;

/**
 * Notification channel configuration.
 * Implementations:
 * <ul>
 *     <li>{@link EmailChannel}</li>
 *     <li>{@link SlackChannel}</li>
 *     <li>{@link PagerDutyChannel}</li>
 *     <li>{@link WebhookChannel}</li>
 *     <li>{@link UserChannel}</li>
 * </ul>
 */
public interface Channel {
    /**
     * Returns channel type. Each Channel implementation should have unique type.
     *
     * @return channel type
     */
    ChannelType getType();

    /**
     * Returns name of your alerts channel.
     *
     * @return channel name
     */
    String getChannelName();

    /**
     * Returns channel type support for this channel type.
     *
     * @return channel type support
     */
    ChannelTypeSupport getChannelTypeSupport();

    /**
     * Returns channel type in string format.
     *
     * @return channel type
     */
    default String getTypeString() {
        return getType().name().toLowerCase();
    }
}
