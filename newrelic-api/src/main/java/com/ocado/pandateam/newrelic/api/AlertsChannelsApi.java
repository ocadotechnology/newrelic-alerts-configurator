package com.ocado.pandateam.newrelic.api;

import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannel;

import java.util.List;

public interface AlertsChannelsApi {
    /**
     * Lists all existing Alerts Channels.
     * This method supports pagination - which means it returns list of entries combined from all pages.
     *
     * @return list of all existing {@link AlertsChannel} from NewRelic
     */
    List<AlertsChannel> list();

    /**
     * Creates Alerts Channel.
     * <p>
     * Although New Relic returns list of channels in its response this method assumes there will be exactly one channel returned.
     *
     * @param channel channel definition to be created
     * @return created {@link AlertsChannel}
     */
    AlertsChannel create(AlertsChannel channel);

    /**
     * Deletes Alerts Channel.
     *
     * @param channelId id of the channel to be deleted
     * @return deleted {@link AlertsChannel}
     */
    AlertsChannel delete(int channelId);

    /**
     * Removes Alerts Channel from Policy definition.
     *
     * @param policyId  id of the policy to be updated
     * @param channelId id of the channel to be removed from the given policy
     * @return {@link AlertsChannel} for the given channel id regardless of being part of the specified policy
     */
    AlertsChannel deleteFromPolicy(int policyId, int channelId);
}
