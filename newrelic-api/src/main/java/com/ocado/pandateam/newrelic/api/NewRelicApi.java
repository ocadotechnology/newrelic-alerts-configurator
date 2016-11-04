package com.ocado.pandateam.newrelic.api;

import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.NewRelicApiRestClient;
import com.ocado.pandateam.newrelic.api.model.Application;
import com.ocado.pandateam.newrelic.api.model.ApplicationList;
import com.ocado.pandateam.newrelic.api.model.User;
import com.ocado.pandateam.newrelic.api.model.UserList;

import java.util.Optional;

/**
 * Object exposing NewRelic API endpoints as Java methods. Requires API key.
 */
public class NewRelicApi {

    private static final String APPLICATIONS_URL = "/v2/applications.json";
    private static final String USERS_URL = "/v2/users.json";

    private final NewRelicApiRestClient rest;

    /**
     * NewRelic API constructor.
     *
     * @param hostUrl NewRelic API host URL, for example https://api.newrelic.com
     * @param apiKey API Key for given NewRelic account
     */
    public NewRelicApi(String hostUrl, String apiKey) {
        rest = new NewRelicApiRestClient(hostUrl, apiKey);
    }

    /**
     * Get {@link Application} object using its name.
     * @param name Name of the application registered in NewRelic
     * @return Optional containing {@link Application} object, or empty if application not found
     * @throws NewRelicApiException when more than one response returned or received error response
     */
    public Optional<Application> getApplicationsByName(String name) throws NewRelicApiException {
        return rest.asSingleObject(ApplicationList.class, rest.get(APPLICATIONS_URL).queryString("filter[name]", name));
    }

    /**
     * Get {@link User} object using its e-mail.
     * @param email E-mail of the user registered in NewRelic
     * @return Optional containing {@link User} object, or empty if application not found
     * @throws NewRelicApiException when more than one response returned or received error response
     */
    public Optional<User> getUserByEmail(String email) throws NewRelicApiException {
        return rest.asSingleObject(UserList.class, rest.get(USERS_URL).queryString("filter[email]", email));
    }

}
