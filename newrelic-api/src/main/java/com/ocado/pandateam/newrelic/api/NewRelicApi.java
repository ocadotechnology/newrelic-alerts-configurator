package com.ocado.pandateam.newrelic.api;

import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.NewRelicApiRestClient;
import com.ocado.pandateam.newrelic.api.model.Application;
import com.ocado.pandateam.newrelic.api.model.ApplicationList;
import com.ocado.pandateam.newrelic.api.model.User;
import com.ocado.pandateam.newrelic.api.model.UserList;

import java.util.Optional;

public class NewRelicApi {

    private static final String APPLICATIONS_URL = "/v2/applications.json";
    private static final String USERS_URL = "/v2/users.json";

    private final NewRelicApiRestClient rest;

    public NewRelicApi(String hostUrl, String apiKey) {
        rest = new NewRelicApiRestClient(hostUrl, apiKey);
    }

    public Optional<Application> getApplicationsByName(String name) throws NewRelicApiException {
        return rest.asSingleObject(ApplicationList.class, rest.get(APPLICATIONS_URL).queryString("filter[name]", name));
    }

    public Optional<User> getUserByEmail(String email) throws NewRelicApiException {
        return rest.asSingleObject(UserList.class, rest.get(USERS_URL).queryString("filter[email]", email));
    }

}
