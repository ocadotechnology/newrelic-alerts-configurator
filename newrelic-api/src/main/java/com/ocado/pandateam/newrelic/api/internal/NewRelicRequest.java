package com.ocado.pandateam.newrelic.api.internal;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiHttpException;
import com.ocado.pandateam.newrelic.api.model.ObjectList;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

public class NewRelicRequest { // TODO: support pagination

    private final HttpRequest httpRequest;

    NewRelicRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest.header("accept", "application/json");
    }

    public NewRelicRequest queryString(String name, Object value) {
        httpRequest.queryString(name, value);
        return this;
    }

    public <T> T asObject(Class<T> responseClass) throws NewRelicApiException {
        try {
            return handleErrorResponse(httpRequest.asObject(responseClass)).getBody();
        } catch (UnirestException e) {
            throw new NewRelicApiException(e);
        }
    }

    public <T> Optional<T> asSingleObject(Class<? extends ObjectList<T>> responseClass)
            throws NewRelicApiException {
        List<T> list = asObject(responseClass).getList();
        if (list.isEmpty()) {
            return Optional.empty();
        } else if (list.size() > 1) {
            throw new NewRelicApiException("Expected single element in the list but found: " + list.size());
        }
        return Optional.of(list.get(0));
    }

    private <T> HttpResponse<? extends T> handleErrorResponse(HttpResponse<? extends T> httpResponse)
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
            return IOUtils.toString(httpResponse.getRawBody(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new NewRelicApiException(e);
        }
    }
}
