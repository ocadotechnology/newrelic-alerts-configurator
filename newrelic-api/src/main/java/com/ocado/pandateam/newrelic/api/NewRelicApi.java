package com.ocado.pandateam.newrelic.api;

import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.NewRelicApiRestClient;
import com.ocado.pandateam.newrelic.api.model.AlertChannel;
import com.ocado.pandateam.newrelic.api.model.AlertChannelList;
import com.ocado.pandateam.newrelic.api.model.AlertChannelWrapper;
import com.ocado.pandateam.newrelic.api.model.AlertPolicy;
import com.ocado.pandateam.newrelic.api.model.AlertPolicyList;
import com.ocado.pandateam.newrelic.api.model.Application;
import com.ocado.pandateam.newrelic.api.model.ApplicationList;
import com.ocado.pandateam.newrelic.api.model.KeyTransaction;
import com.ocado.pandateam.newrelic.api.model.KeyTransactionList;
import com.ocado.pandateam.newrelic.api.model.User;
import com.ocado.pandateam.newrelic.api.model.UserList;

import java.util.List;
import java.util.Optional;

/**
 * Object exposing NewRelic API endpoints as Java methods. Requires API key.
 */
public class NewRelicApi {

    private static final String NEWRELIC_HOST_URL = "https://api.newrelic.com";

    static final String APPLICATIONS = "/v2/applications.json";
    private static final String USERS = "/v2/users.json";
    private static final String KEY_TRANSACTIONS = "/v2/key_transactions.json";
    private static final String ALERTS_CHANNELS = "/v2/alerts_channels.json";
    private static final String ALERTS_POLICIES = "/v2/alerts_policies.json";

    private final NewRelicApiRestClient api;

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
     * @param apiKey API Key for given NewRelic account
     */
    public NewRelicApi(String hostUrl, String apiKey) {
        api = new NewRelicApiRestClient(hostUrl, apiKey);
    }

    /**
     * Get {@link Application} object using its name.
     * @param name Name of the application registered in NewRelic
     * @return Optional containing {@link Application} object, or empty if application not found
     * @throws NewRelicApiException when more than one response returned or received error response
     */
    public Optional<Application> getApplicationByName(String name) throws NewRelicApiException {
        return api.get(APPLICATIONS).queryString("filter[name]", name).asSingleObject(ApplicationList.class);
    }

    /**
     * Get {@link User} object using its e-mail.
     * @param email E-mail of the user present in NewRelic
     * @return Optional containing {@link User} object, or empty if application not found
     * @throws NewRelicApiException when more than one response returned or received error response
     */
    public Optional<User> getUserByEmail(String email) throws NewRelicApiException {
        return api.get(USERS).queryString("filter[email]", email).asSingleObject(UserList.class);
    }

    /**
     * Get {@link KeyTransaction} object using its name.
     * @param name Name of the KeyTransaction present in NewRelic
     * @return Optional containing {@link KeyTransaction} object, or empty if application not found
     * @throws NewRelicApiException when more than one response returned or received error response
     */
    public Optional<KeyTransaction> getKeyTransactionByName(String name) throws NewRelicApiException {
        return api.get(KEY_TRANSACTIONS).queryString("filter[name]", name).asSingleObject(KeyTransactionList.class);
    }

    /**
     * List all existing Alert Channels.
     * @return list of all existing {@link AlertChannel} from NewRelic
     * @throws NewRelicApiException when received error response
     */
    public List<AlertChannel> listAlertChannels() throws NewRelicApiException {
        return api.get(ALERTS_CHANNELS).asObject(AlertChannelList.class).getList();
    }

    /**
     * Creates {@link AlertChannel} of type "user".
     * @param name Name of the Alert Channel to be created
     * @param userId Identifier of the user entity from NewRelic
     * @return created {@link AlertChannel} instance in NewRelic
     * @throws NewRelicApiException
     */
    public AlertChannel createUserAlertChannel(String name, String userId) throws NewRelicApiException {
        return api.post(ALERTS_CHANNELS)
                .body(new AlertChannelWrapper(AlertChannel.createForUser(name, userId)))
                .asObject(AlertChannelWrapper.class)
                .getChannel();
    }

    /**
     * Creates {@link AlertChannel} of type "email".
     * @param name Name of the Alert Channel to be created
     * @param recipients E-mail address of recipients
     * @param includeJsonAttachment
     * @return created {@link AlertChannel} instance in NewRelic
     * @throws NewRelicApiException
     */
    public AlertChannel createEmailAlertChannel(String name, String recipients, String includeJsonAttachment)
            throws NewRelicApiException {
        return api.post(ALERTS_CHANNELS)
                .body(new AlertChannelWrapper(AlertChannel.createForEmail(name, recipients, includeJsonAttachment)))
                .asObject(AlertChannelWrapper.class)
                .getChannel();
    }

    /**
     * Creates {@link AlertChannel} of type "slack".
     * @param name Name of the Alert Channel to be created
     * @param url (Optional) URL address of Slack
     * @param channel
     * @return created {@link AlertChannel} instance in NewRelic
     * @throws NewRelicApiException
     */
    public AlertChannel createSlackAlertChannel(String name, String url, String channel) throws NewRelicApiException {
        return api.post(ALERTS_CHANNELS)
                .body(new AlertChannelWrapper(AlertChannel.createForSlack(name, url, channel)))
                .asObject(AlertChannelWrapper.class)
                .getChannel();
    }

    /**
     * Get {@link AlertPolicy} object using its name.
     *
     * @param name Name of the alert policy registered in NewRelic
     * @return Optional containing {@link AlertPolicy} object, or empty if alert policy not found
     * @throws NewRelicApiException when more than one response returned or received error response
     */
    public Optional<AlertPolicy> getAlertPolicyByName(String name) throws NewRelicApiException {
        return api.get(ALERTS_POLICIES).queryString("filter[name]", name).asSingleObject(AlertPolicyList.class);
    }
}
