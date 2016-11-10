package com.ocado.pandateam.newrelic.api.internal;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.ObjectList;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class NewRelicRequest { // TODO: support pagination

    private final HttpRequest request;

    private NewRelicRequest(HttpRequest request) {
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

    private <T> T execute(Class<T> responseClass) throws UnirestException {
        logRequest();
        NewRelicResponse<T> response = getResponse(responseClass);
        response.log();
        if (response.isSuccess()) {
            return response.getBody();
        } else {
            throw new RuntimeException();
        }
    }

    private <T> NewRelicResponse<T> getResponse(Class<T> responseClass) throws UnirestException {
        return new NewRelicResponse<>(request.asObject(responseClass));
    }

    public static class NewRelicGetRequest extends NewRelicRequest {

        private final HttpRequest getRequest;

        NewRelicGetRequest(GetRequest getRequest) {
            super(getRequest);
            this.getRequest = getRequest;
        }

        public NewRelicGetRequest queryString(String name, Object value) {
            getRequest.queryString(name, value);
            return this;
        }

        @Override
        protected void logRequest() {
            log.info("{} {}", getRequest.getHttpMethod(), getRequest.getUrl());
        }
    }

    public static class NewRelicRequestWithBody extends NewRelicRequest {

        private final HttpRequestWithBody requestWithBody;

        private Object body;

        NewRelicRequestWithBody(HttpRequestWithBody requestWithBody) {
            super(requestWithBody);
            this.requestWithBody = requestWithBody;
        }

        public NewRelicRequestWithBody body(Object body) {
            this.body = body;
            requestWithBody.header("Content-Type", "application/json");
            requestWithBody.body(body);
            return this;
        }

        @Override
        protected void logRequest() {
            log.info("{} {}", requestWithBody.getHttpMethod(), requestWithBody.getUrl());
            log.info("{}", body);
        }
    }

    private class NewRelicResponse<T> {

        private final HttpResponse<T> response;

        private NewRelicResponse(HttpResponse<T> response) {
            this.response = response;
        }

        boolean isSuccess() {
            return 200 <= response.getStatus() && response.getStatus() < 300;
        }

        T getBody() {
            return response.getBody();
        }

        void log() {
            log.info("{} {}", response.getStatus(), response.getStatusText());
            if (response.getBody() != null) {
                log.info("{}", response.getBody());
            }
        }
    }

    protected abstract void logRequest();

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
