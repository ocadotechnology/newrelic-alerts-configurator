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

public class AlertsChannelsApi extends BaseApi {

    private static final String CHANNELS_URL = "/v2/alerts_channels.json";
    private static final String CHANNEL_URL = "/v2/alerts_channels/{channel_id}.json";
    private static final String POLICY_CHANNELS_URL = "/v2/alerts_policy_channels.json";

    AlertsChannelsApi(NewRelicRestClient api) {
        super(api);
    }

    /**
     * List all existing Alert Channels.
     *
     * @return list of all existing {@link AlertsChannel} from NewRelic
     * @throws NewRelicApiException when received error response
     */
    public List<AlertsChannel> list() throws NewRelicApiException {
        GetRequest request = api.get(CHANNELS_URL);
        return api.asObject(request, AlertsChannelList.class).getList();
    }

    public AlertsChannel create(AlertsChannel channel) throws NewRelicApiException {
        RequestBodyEntity request = api.post(CHANNELS_URL).body(new AlertsChannelWrapper(channel));
        return api.asObject(request, AlertsChannelList.class).getSingle()
                .orElseThrow(() -> new NewRelicApiException("Failed to create channel"));
    }

    public AlertsChannel delete(int channelId) throws NewRelicApiException {
        HttpRequest request = api.delete(CHANNEL_URL).routeParam("channel_id", String.valueOf(channelId));
        return api.asObject(request, AlertsChannelWrapper.class).getChannel();
    }

    public AlertsChannel deleteFromPolicy(int policyId, int channelId) throws NewRelicApiException {
        HttpRequest request = api.delete(POLICY_CHANNELS_URL)
                .queryString("policy_id", policyId)
                .queryString("channel_id", channelId);
        return api.asObject(request, AlertsChannelWrapper.class).getChannel();
    }
}
