package com.ocado.pandateam.newrelic.api;

import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.RequestBodyEntity;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.NewRelicRestClient;
import com.ocado.pandateam.newrelic.api.model.channels.AlertChannel;
import com.ocado.pandateam.newrelic.api.model.channels.AlertChannelList;
import com.ocado.pandateam.newrelic.api.model.channels.AlertChannelWrapper;

import java.util.List;
import java.util.Optional;

public class AlertsChannelsApi extends BaseApi {

    private static final String CHANNELS_URL = "/v2/alerts_channels.json";
    private static final String CHANNEL_URL = "/v2/alerts_channels/{channel_id}.json";

    AlertsChannelsApi(NewRelicRestClient api) {
        super(api);
    }

    /**
     * List all existing Alert Channels.
     *
     * @return list of all existing {@link AlertChannel} from NewRelic
     * @throws NewRelicApiException when received error response
     */
    public List<AlertChannel> list() throws NewRelicApiException {
        GetRequest request = api.get(CHANNELS_URL);
        return api.asObject(request, AlertChannelList.class).getList();
    }

    public AlertChannel create(AlertChannel channel) throws NewRelicApiException {
        RequestBodyEntity request = api.post(CHANNELS_URL).body(new AlertChannelWrapper(channel));
        return api.asObject(request, AlertChannelList.class).getSingle()
                .orElseThrow(() -> new NewRelicApiException("Failed to create channel"));
    }

    public AlertChannel delete(int channelId) throws NewRelicApiException {
        HttpRequest request = api.delete(CHANNEL_URL).routeParam("channel_id", String.valueOf(channelId));
        return api.asObject(request, AlertChannelWrapper.class).getChannel();
    }

    /**
     * Creates {@link AlertChannel} of type "email".
     *
     * @param name                  Name of the Alert Channel to be created
     * @param recipients            E-mail address of recipients
     * @param includeJsonAttachment flag determining if email alert should include attachment
     * @return created {@link AlertChannel} instance in NewRelic
     * @throws NewRelicApiException when received error response
     */
    public Optional<AlertChannel> createEmailAlertChannel(String name, String recipients, String includeJsonAttachment)
            throws NewRelicApiException {
        RequestBodyEntity request = api.post(CHANNELS_URL)
                .body(new AlertChannelWrapper(AlertChannel.createForEmail(name, recipients, includeJsonAttachment)));
        return api.asObject(request, AlertChannelList.class).getSingle();
    }

    /**
     * Creates {@link AlertChannel} of type "slack".
     *
     * @param name    Name of the Alert Channel to be created
     * @param url     URL address of Slack
     * @param channel (Optional) Name of the Slack channel for example: #channel-name
     * @return created {@link AlertChannel} instance in NewRelic
     * @throws NewRelicApiException when received error response
     */
    public Optional<AlertChannel> createSlackAlertChannel(String name, String url, String channel) throws NewRelicApiException {
        RequestBodyEntity request = api.post(CHANNELS_URL)
                .body(new AlertChannelWrapper(AlertChannel.createForSlack(name, url, channel)));
        return api.asObject(request, AlertChannelList.class).getSingle();
    }
}
