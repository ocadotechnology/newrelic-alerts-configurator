package com.ocado.pandateam.newrelic.api.internal;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiHttpException;
import com.ocado.pandateam.newrelic.api.model.ObjectList;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
// TODO: support pagination
public abstract class NewRelicRequest {

    private final HttpRequest request;

    NewRelicRequest(HttpRequest request) {
        this.request = request.header("accept", "application/json");
    }

    public <T> Optional<T> asSingleObject(Class<? extends ObjectList<T>> responseClass)
            throws NewRelicApiException {
        List<T> list = asObject(responseClass).getList();
        if (list.isEmpty()) {
            return Optional.empty();
        }
        if (list.size() == 1) {
            return Optional.of(list.get(0));
        }
        throw new NewRelicApiException("Expected single element in the list but found: " + list.size());
    }

    public <T> T asObject(Class<T> responseClass) throws NewRelicApiException {
        try {
            return execute(responseClass);
        } catch (UnirestException e) {
            throw new NewRelicApiException(e);
        }
    }

    private <T> T execute(Class<T> responseClass) throws UnirestException, NewRelicApiHttpException {
        logRequest();
        NewRelicResponse<T> response = getResponse(responseClass);
        response.log();
        if (response.isSuccess()) {
            return response.getBody();
        } else {
            throw new NewRelicApiHttpException(request, response.getRawResponse());
        }
    }

    private <T> NewRelicResponse<T> getResponse(Class<T> responseClass) throws UnirestException {
        return new NewRelicResponse<>(request.asObject(responseClass));
    }

    protected abstract void logRequest();
}
