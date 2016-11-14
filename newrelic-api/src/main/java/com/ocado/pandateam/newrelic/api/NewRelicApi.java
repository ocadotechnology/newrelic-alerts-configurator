package com.ocado.pandateam.newrelic.api;

import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.MultipartBody;
import com.mashape.unirest.request.body.RequestBodyEntity;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.NewRelicRestClient;
import com.ocado.pandateam.newrelic.api.model.AlertChannel;
import com.ocado.pandateam.newrelic.api.model.AlertChannelList;
import com.ocado.pandateam.newrelic.api.model.AlertChannelWrapper;
import com.ocado.pandateam.newrelic.api.model.AlertPolicy;
import com.ocado.pandateam.newrelic.api.model.AlertPolicyChannels;
import com.ocado.pandateam.newrelic.api.model.AlertPolicyChannelsWrapper;
import com.ocado.pandateam.newrelic.api.model.AlertPolicyList;
import com.ocado.pandateam.newrelic.api.model.AlertPolicyWrapper;
import com.ocado.pandateam.newrelic.api.model.Application;
import com.ocado.pandateam.newrelic.api.model.ApplicationList;
import com.ocado.pandateam.newrelic.api.model.ApplicationWrapper;
import com.ocado.pandateam.newrelic.api.model.KeyTransaction;
import com.ocado.pandateam.newrelic.api.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Object exposing NewRelic API endpoints as Java methods. Requires API key.
 */
public class NewRelicApi {

    private static final String NEWRELIC_HOST_URL = "https://api.newrelic.com";

    private static final String ALL_APPLICATIONS = "/v2/applications.json";
    private static final String APPLICATIONS = "/v2/applications/{id}.json";

    private static final String USERS = "/v2/users.json";
    private static final String KEY_TRANSACTIONS = "/v2/key_transactions.json";
    private static final String ALERTS_CHANNELS = "/v2/alerts_channels.json";
    private static final String ALERTS_POLICIES = "/v2/alerts_policies.json";

    private static final String ALERTS_POLICY_CHANNELS = "/v2/alerts_policy_channels.json";

    private final NewRelicRestClient api;

    /**
     * NewRelic API constructor.
     *
     * @param apiKey API Key for given NewRelic account
     */
    public NewRelicApi(String apiKey) {
        this(NEWRELIC_HOST_URL, apiKey);
    }

    /**
     * NewRelic API constructor.
     *
     * @param hostUrl NewRelic API host URL, for example https://api.newrelic.com
     * @param apiKey  API Key for given NewRelic account
     */
    public NewRelicApi(String hostUrl, String apiKey) {
        api = new NewRelicRestClient(hostUrl, apiKey);
    }

    /**
     * Get {@link Application} object using its name.
     *
     * @param name Name of the application registered in NewRelic
     * @return Optional containing {@link Application} object, or empty if application not found
     * @throws NewRelicApiException when more than one response returned or received error response
     */
    public Optional<Application> getApplicationByName(String name) throws NewRelicApiException {
        HttpRequest request = api.get(ALL_APPLICATIONS).queryString("filter[name]", name);
        return api.asObject(request, ApplicationList.class).getSingle();
    }

    /**
     * Updates {@link Application} object.
     *
     * @param application Application to be updated.
     * @return Updated {@link Application}.
     * @throws NewRelicApiException when received error response
     */
    public Application updateApplication(int id, Application application) throws NewRelicApiException {
        RequestBodyEntity request = api.put(APPLICATIONS)
                .routeParam("id", String.valueOf(id))
                .body(new ApplicationWrapper(application));
        return api.asObject(request, ApplicationWrapper.class).getApplication();
    }

    /**
     * Get {@link User} object using its e-mail.
     *
     * @param email E-mail of the user present in NewRelic
     * @return Optional containing {@link User} object, or empty if application not found
     * @throws NewRelicApiException when more than one response returned or received error response
     *//*
    public Optional<User> getUserByEmail(String email) throws NewRelicApiException {
        return api.get(USERS).queryString("filter[email]", email).asSingleObject(UserList.class);
    }*/

    /**
     * Get {@link KeyTransaction} object using its name.
     *
     * @param name Name of the KeyTransaction present in NewRelic
     * @return Optional containing {@link KeyTransaction} object, or empty if application not found
     * @throws NewRelicApiException when more than one response returned or received error response
     *//*
    public Optional<KeyTransaction> getKeyTransactionByName(String name) throws NewRelicApiException {
        return api.get(KEY_TRANSACTIONS).queryString("filter[name]", name).asSingleObject(KeyTransactionList.class);
    }*/

    /**
     * List all existing Alert Channels.
     *
     * @return list of all existing {@link AlertChannel} from NewRelic
     * @throws NewRelicApiException when received error response
     */
    public List<AlertChannel> listAlertChannels() throws NewRelicApiException {
        GetRequest request = api.get(ALERTS_CHANNELS);
        return api.asObject(request, AlertChannelList.class).getList();
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
        RequestBodyEntity request = api.post(ALERTS_CHANNELS)
                .body(new AlertChannelWrapper(AlertChannel.createForEmail(name, recipients, includeJsonAttachment)));
        return api.asObject(request, AlertChannelList.class).getSingle();
    }

    /**
     * Creates {@link AlertChannel} of type "slack".
     *
     * @param name    Name of the Alert Channel to be created
     * @param url     (Optional) URL address of Slack
     * @param channel Name of the Slack channel for example: #channel-name
     * @return created {@link AlertChannel} instance in NewRelic
     * @throws NewRelicApiException when received error response
     *//*
    public Optional<AlertChannel> createSlackAlertChannel(String name, String url, String channel) throws NewRelicApiException {
        return api.post(ALERTS_CHANNELS)
                .body(new AlertChannelWrapper(AlertChannel.createForSlack(name, url, channel)))
                .asSingleObject(AlertChannelList.class);
    }*/

    /**
     * Get {@link AlertPolicy} object using its name.
     *
     * @param name Name of the alert policy registered in NewRelic
     * @return Optional containing {@link AlertPolicy} object, or empty if alert policy not found
     * @throws NewRelicApiException when more than one response returned or received error response
     */
    public Optional<AlertPolicy> getAlertPolicyByName(String name) throws NewRelicApiException {
        HttpRequest request = api.get(ALERTS_POLICIES).queryString("filter[name]", name);
        return api.asObject(request, AlertPolicyList.class).getSingle();
    }

    /**
     * Creates {@link AlertPolicy} object using its name.
     *
     * @param name Name of the alert policy to be created
     * @return {@link AlertPolicy} object
     * @throws NewRelicApiException when received error response
     */
    public AlertPolicy createAlertPolicy(String name) throws NewRelicApiException {
        RequestBodyEntity request = api.post(ALERTS_POLICIES)
                .body(new AlertPolicyWrapper(AlertPolicy.builder()
                        .name(name)
                        .incidentPreference("PER_POLICY")
                        .build()));
        return api.asObject(request, AlertPolicyWrapper.class).getPolicy();
    }

    public AlertPolicyChannels updateAlertsPolicyChannels(AlertPolicyChannels alertPolicyChannels) throws NewRelicApiException {
        MultipartBody request = api.put(ALERTS_POLICY_CHANNELS)
                .field("policy_id", alertPolicyChannels.getPolicyId())
                .field("channel_ids",
                        alertPolicyChannels.getChannelIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
        return api.asObject(request, AlertPolicyChannelsWrapper.class).getPolicyChannels();
    }
}
