package com.ocado.pandateam.newrelic.api;

import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannel;

import java.util.List;

public interface AlertsChannelsApi {
    /**
     * Lists all existing Alerts Channels.
     *
     * @return list of all existing {@link AlertsChannel} from NewRelic
     * @throws NewRelicApiException when received error response
     */
    List<AlertsChannel> list() throws NewRelicApiException;

    /**
     * Creates Alerts Channel.
     * <p>
     * Although New Relic returns list of channels in its response this method assumes there will be exactly one channel returned.
     *
     * @param channel - channel definition to be created
     * @return created {@link AlertsChannel}
     * @throws NewRelicApiException when received error response or when multiple channels created
     */
    AlertsChannel create(AlertsChannel channel) throws NewRelicApiException;

    /**
     * Deletes Alerts Channel.
     *
     * @param channelId - id of the channel to be deleted
     * @return deleted {@link AlertsChannel}
     * @throws NewRelicApiException when received error response
     */
    AlertsChannel delete(int channelId) throws NewRelicApiException;

    /**
     * Removes Alerts Channel from Policy definition.
     *
     * @param policyId  - id of the policy to be updated
     * @param channelId - id of the channel to be removed from the given policy
     * @return {@link AlertsChannel} for the given channel id regardless of being part of the specified policy
     * @throws NewRelicApiException when received error response
     */
    AlertsChannel deleteFromPolicy(int policyId, int channelId) throws NewRelicApiException;
}
