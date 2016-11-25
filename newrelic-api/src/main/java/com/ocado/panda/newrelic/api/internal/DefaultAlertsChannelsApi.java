package com.ocado.panda.newrelic.api.internal;

import com.ocado.panda.newrelic.api.internal.model.AlertsChannelWrapper;
import com.ocado.panda.newrelic.api.AlertsChannelsApi;
import com.ocado.panda.newrelic.api.internal.client.NewRelicClient;
import com.ocado.panda.newrelic.api.internal.model.AlertsChannelList;
import com.ocado.panda.newrelic.api.model.channels.AlertsChannel;

import javax.ws.rs.client.Entity;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

class DefaultAlertsChannelsApi extends ApiBase implements AlertsChannelsApi {

    private static final String CHANNELS_URL = "/v2/alerts_channels.json";
    private static final String CHANNEL_URL = "/v2/alerts_channels/{channel_id}.json";
    private static final String POLICY_CHANNELS_URL = "/v2/alerts_policy_channels.json";

    DefaultAlertsChannelsApi(NewRelicClient client) {
        super(client);
    }

    @Override
    public List<AlertsChannel> list() {
        return getPageable(
                client.target(CHANNELS_URL).request(APPLICATION_JSON_TYPE),
                AlertsChannelList.class,
                AlertsChannelList::merge).getList();
    }

    @Override
    public AlertsChannel create(AlertsChannel channel) {
        return client
                .target(CHANNELS_URL)
                .request(APPLICATION_JSON_TYPE)
                .post(Entity.entity(new AlertsChannelWrapper(channel), APPLICATION_JSON_TYPE), AlertsChannelList.class)
                .getSingle()
                .orElseThrow(() -> new IllegalStateException("Failed to create channel: empty list returned"));
    }

    @Override
    public AlertsChannel delete(int channelId) {
        return client
                .target(CHANNEL_URL)
                .resolveTemplate("channel_id", channelId)
                .request(APPLICATION_JSON_TYPE)
                .delete(AlertsChannelWrapper.class)
                .getChannel();
    }

    @Override
    public AlertsChannel deleteFromPolicy(int policyId, int channelId) {
        return client
                .target(POLICY_CHANNELS_URL)
                .queryParam("policy_id", policyId)
                .queryParam("channel_id", channelId)
                .request(APPLICATION_JSON_TYPE)
                .delete(AlertsChannelWrapper.class)
                .getChannel();
    }
}
