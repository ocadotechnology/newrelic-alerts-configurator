package com.ocado.pandateam.newrelic.api;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.Mapper;
import com.ocado.pandateam.newrelic.api.model.User;
import com.ocado.pandateam.newrelic.api.model.UserList;

public class NewRelicApi {

    private final String hostUrl;
    private final String apiKey;

    public NewRelicApi(String hostUrl, String apiKey) {
        this.hostUrl = hostUrl;
        this.apiKey = apiKey;
        Unirest.setObjectMapper(new Mapper());
    }

    public String getApplicationsByName(String name) throws NewRelicApiException {
        return asString(get("/v2/applications.json").queryString("filter[name]", name));
    }

    public User getUserByEmail(String email) throws NewRelicApiException {
        return asObject(UserList.class, get("/v2/users.json").queryString("filter[email]", email)).getUsers().get(0);
    }

    private GetRequest get(String url) {
        return Unirest.get(hostUrl + url).header("X-Api-Key", apiKey);
    }

    private String asString(HttpRequest httpRequest) throws NewRelicApiException {
        try {
            return httpRequest.asString().getBody();
        } catch (UnirestException e) {
            throw new NewRelicApiException(e);
        }
    }

    private <T> T asObject(Class<? extends T> responseClass, HttpRequest httpRequest) throws NewRelicApiException {
        try {
            return httpRequest.asObject(responseClass).getBody();
        } catch (UnirestException e) {
            throw new NewRelicApiException(e);
        }
    }

}
