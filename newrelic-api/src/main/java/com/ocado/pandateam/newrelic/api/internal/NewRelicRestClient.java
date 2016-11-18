package com.ocado.pandateam.newrelic.api.internal;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.BaseRequest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiHttpException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.ocado.pandateam.newrelic.api.internal.NewRelicRequestConstants.ACCEPT_HEADER;
import static com.ocado.pandateam.newrelic.api.internal.NewRelicRequestConstants.APPLICATION_JSON;
import static com.ocado.pandateam.newrelic.api.internal.NewRelicRequestConstants.APPLICATION_JSON_UTF_8;
import static com.ocado.pandateam.newrelic.api.internal.NewRelicRequestConstants.CONTENT_TYPE_HEADER;
import static com.ocado.pandateam.newrelic.api.internal.NewRelicRequestConstants.X_API_KEY_HEADER;

@Slf4j
class NewRelicRestClient {

    private final String hostUrl;
    private final String apiKey;

    NewRelicRestClient(String hostUrl, String apiKey) {
        this.hostUrl = hostUrl;
        this.apiKey = apiKey;
        Unirest.setObjectMapper(new NewRelicRequestMapper());
    }

    GetRequest get(String url) {
        return prepareRequest(Unirest.get(hostUrl + url));
    }

    HttpRequestWithBody put(String url) {
        return put(url, APPLICATION_JSON_UTF_8);
    }

    HttpRequestWithBody put(String url, String contentType) {
        return prepareRequest(Unirest.put(hostUrl + url), contentType);
    }

    HttpRequestWithBody post(String url) {
        return post(url, APPLICATION_JSON_UTF_8);
    }

    HttpRequestWithBody post(String url, String contentType) {
        return prepareRequest(Unirest.post(hostUrl + url), contentType);
    }

    HttpRequest delete(String url) {
        return delete(url, APPLICATION_JSON_UTF_8);
    }

    HttpRequest delete(String url, String contentType) {
        return prepareRequest(Unirest.delete(hostUrl + url), contentType);
    }

    <T> T asObject(BaseRequest request, Class<T> responseType) throws NewRelicApiException {
        try {
            logRequest(request);
            HttpResponse<T> response = request.asObject(responseType);
            logResponse(request, response);
            return handleResponse(request, response);
        } catch (UnirestException e) {
            throw new NewRelicApiException(e);
        }
    }

    private GetRequest prepareRequest(GetRequest request) {
        return request
                .header(X_API_KEY_HEADER, apiKey)
                .header(ACCEPT_HEADER, APPLICATION_JSON);
    }

    private HttpRequestWithBody prepareRequest(HttpRequestWithBody request, String contentType) {
        return request
                .header(X_API_KEY_HEADER, apiKey)
                .header(ACCEPT_HEADER, APPLICATION_JSON)
                .header(CONTENT_TYPE_HEADER, contentType);
    }

    private void logRequest(BaseRequest request) {
        log.info("{} {}",
                request.getHttpRequest().getHttpMethod(),
                request.getHttpRequest().getUrl());
    }

    private <T> void logResponse(BaseRequest request, HttpResponse<T> response) {
        log.info("{} {}: {} {}",
                request.getHttpRequest().getHttpMethod(),
                request.getHttpRequest().getUrl(),
                response.getStatus(),
                response.getStatusText());
    }

    private <T> void logResponseBody(HttpResponse<T> response) {
        try {
            log.info("{}", IOUtils.toString(response.getRawBody(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("Failed to log response body", e);
        }
    }

    private <T> T handleResponse(BaseRequest request, HttpResponse<T> response) throws NewRelicApiHttpException {
        if (200 <= response.getStatus() && response.getStatus() < 300) {
            return response.getBody();
        } else {
            logResponseBody(response);
            throw new NewRelicApiHttpException(request, response);
        }
    }
}
