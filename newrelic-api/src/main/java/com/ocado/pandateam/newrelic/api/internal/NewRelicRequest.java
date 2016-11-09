package com.ocado.pandateam.newrelic.api.internal;

import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.ObjectList;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class NewRelicRequest { // TODO: support pagination

    private final HttpRequest request;

    NewRelicRequest(HttpRequest request) {
        this.request = request.header("accept", "application/json");
    }

    public NewRelicRequest queryString(String name, Object value) {
        request.queryString(name, value);
        return this;
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
            HttpResponse<T> httpResponse = executeRequest(responseClass);
            return handleResponse(httpResponse);
        } catch (UnirestException e) {
            throw new NewRelicApiException(e);
        }
    }

    private <T> HttpResponse<T> executeRequest(Class<T> responseClass) throws UnirestException {
        log.info("{} {}", request.getHttpMethod(), request.getUrl());
        if (request.getHttpMethod() == HttpMethod.POST) {
            log.info("{}", request.getBody());
        }
        return request.asObject(responseClass);
    }

    private <T> T handleResponse(HttpResponse<T> response) throws NewRelicApiException {
        log.info("{} {}", response.getStatus(), response.getStatusText());
        if (200 <= response.getStatus() && response.getStatus() < 300) {
            log.info("{}", response.getBody());
            return response.getBody();
        } else {
            //FIXME
            throw new RuntimeException();
        }
    }

    /*private <T> HttpResponse<? extends T> handleResponse(HttpResponse<? extends T> httpResponse)
            throws NewRelicApiException {
        int status = httpResponse.getStatus();
        if (status < 200 || 300 <= status) {
            throw new NewRelicApiHttpException(httpResponse.getStatus(), httpResponse.getStatusText(),
                    httpRequest.getHttpMethod().name(), httpRequest.getUrl(), getRawResponse(httpResponse));
        }
        return httpResponse;
    }

    private static <T> String getRawResponse(HttpResponse<? extends T> httpResponse) throws NewRelicApiException {
        try {
            return IOUtils.toString(httpResponse.getRawBody(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new NewRelicApiException(e);
        }
    }*/
}
