package com.ocado.pandateam.newrelic.api;

import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.NewRelicApiRestClient;
import com.ocado.pandateam.newrelic.api.model.AlertChannel;
import com.ocado.pandateam.newrelic.api.model.AlertChannelList;
import com.ocado.pandateam.newrelic.api.model.Application;
import com.ocado.pandateam.newrelic.api.model.ApplicationList;
import com.ocado.pandateam.newrelic.api.model.KeyTransaction;
import com.ocado.pandateam.newrelic.api.model.KeyTransactionList;
import com.ocado.pandateam.newrelic.api.model.AlertChannelWrapper;
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
    public Optional<Application> getApplicationsByName(String name) throws NewRelicApiException {
        return api.get(APPLICATIONS).queryString("filter[name]", name).asSingleObject(ApplicationList.class);
    }

    /**
     * Get {@link User} object using its e-mail.
     * @param email E-mail of the user registered in NewRelic
     * @return Optional containing {@link User} object, or empty if application not found
     * @throws NewRelicApiException when more than one response returned or received error response
     */
    public Optional<User> getUserByEmail(String email) throws NewRelicApiException {
        return api.get(USERS).queryString("filter[email]", email).asSingleObject(UserList.class);
    }

    public Optional<KeyTransaction> getKeyTransactionByName(String name) throws NewRelicApiException {
        return api.get(KEY_TRANSACTIONS).queryString("filter[name]", name).asSingleObject(KeyTransactionList.class);
    }

    public List<AlertChannel> listAlertChannels() throws NewRelicApiException {
        return api.get(ALERTS_CHANNELS).asObject(AlertChannelList.class).getList();
    }

    public AlertChannel createUserAlertChannel(String name, String userId) throws NewRelicApiException {
        return api.post(ALERTS_CHANNELS)
                .body(new AlertChannelWrapper(AlertChannel.createForUser(name, userId)))
                .asObject(AlertChannel.class);
    }

    public AlertChannel createEmailAlertChannel(String name, String recipients, String includeJsonAttachment)
            throws NewRelicApiException {
        return api.post(ALERTS_CHANNELS)
                .body(new AlertChannelWrapper(AlertChannel.createForEmail(name, recipients, includeJsonAttachment)))
                .asObject(AlertChannel.class);
    }

    public AlertChannel createSlackAlertChannel(String name, String url, String channel) throws NewRelicApiException {
        return api.post(ALERTS_CHANNELS)
                .body(new AlertChannelWrapper(AlertChannel.createForSlack(name, url, channel)))
                .asObject(AlertChannel.class);
    }

}
