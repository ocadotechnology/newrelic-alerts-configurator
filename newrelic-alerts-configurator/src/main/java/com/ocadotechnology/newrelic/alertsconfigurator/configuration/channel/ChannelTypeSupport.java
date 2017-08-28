package com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel;

import com.ocadotechnology.newrelic.apiclient.NewRelicApi;
import com.ocadotechnology.newrelic.apiclient.model.channels.AlertsChannelConfiguration;

/**
 * This interface represents support object, that is used to transform implementation of {@link Channel} to
 * {@link AlertsChannelConfiguration}, thet will be used by api client.
 * Each implementation of {@link Channel} should provide it's own implementation of this interface, that will build correct
 * object of {@link AlertsChannelConfiguration}.
 */
public interface ChannelTypeSupport {

    /**
     * Builds alerts channel configuration.
     *
     * @param api NewRelic api client. Can be used to obtain additional paremeters required to build alerts channel
     *            configuration, like {@code userId}
     * @return generated alerts channel configuration
     */
    AlertsChannelConfiguration generateAlertsChannelConfiguration(NewRelicApi api);
}
