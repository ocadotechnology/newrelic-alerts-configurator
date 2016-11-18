package com.ocado.pandateam.newrelic.api;

import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.RequestBodyEntity;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.NewRelicRestClient;
import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannel;
import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannelList;
import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannelWrapper;

import java.util.List;

public class AlertsChannelsApi extends ApiBase {

    private static final String CHANNELS_URL = "/v2/alerts_channels.json";
    private static final String CHANNEL_URL = "/v2/alerts_channels/{channel_id}.json";
    private static final String POLICY_CHANNELS_URL = "/v2/alerts_policy_channels.json";

    AlertsChannelsApi(NewRelicRestClient api) {
        super(api);
    }

    /**
     * Lists all existing Alerts Channels.
     *
     * @return list of all existing {@link AlertsChannel} from NewRelic
     * @throws NewRelicApiException when received error response
     */
    public List<AlertsChannel> list() throws NewRelicApiException {
        GetRequest request = api.get(CHANNELS_URL);
        return api.asObject(request, AlertsChannelList.class).getList();
    }

    /**
     * Creates Alerts Channel.
     * <p>
     * Although New Relic returns list of channels in its response this method assumes there will be exactly one channel returned.
     *
     * @param channel - channel definition to be created
     * @return created {@link AlertsChannel}
     * @throws NewRelicApiException when received error response or when multiple channels created
     */
    public AlertsChannel create(AlertsChannel channel) throws NewRelicApiException {
        RequestBodyEntity request = api.post(CHANNELS_URL).body(new AlertsChannelWrapper(channel));
        return api.asObject(request, AlertsChannelList.class).getSingle()
                .orElseThrow(() -> new NewRelicApiException("Failed to create channel"));
    }

    /**
     * Deletes Alerts Channel.
     *
     * @param channelId - id of the channel to be deleted
     * @return deleted {@link AlertsChannel}
     * @throws NewRelicApiException when received error response
     */
    public AlertsChannel delete(int channelId) throws NewRelicApiException {
        HttpRequest request = api.delete(CHANNEL_URL).routeParam("channel_id", String.valueOf(channelId));
        return api.asObject(request, AlertsChannelWrapper.class).getChannel();
    }

    /**
     * Removes Alerts Channel from Policy definition.
     *
     * @param policyId  - id of the policy to be updated
     * @param channelId - id of the channel to be removed from the given policy
     * @return {@link AlertsChannel} for the given channel id regardless of being part of the specified policy
     * @throws NewRelicApiException when received error response
     */
    public AlertsChannel deleteFromPolicy(int policyId, int channelId) throws NewRelicApiException {
        HttpRequest request = api.delete(POLICY_CHANNELS_URL)
                .queryString("policy_id", policyId)
                .queryString("channel_id", channelId);
        return api.asObject(request, AlertsChannelWrapper.class).getChannel();
    }
}
