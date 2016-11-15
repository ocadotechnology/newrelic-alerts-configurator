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

@Slf4j
public class NewRelicRestClient {

    private final String hostUrl;
    private final String apiKey;

    public NewRelicRestClient(String hostUrl, String apiKey) {
        this.hostUrl = hostUrl;
        this.apiKey = apiKey;
        Unirest.setObjectMapper(new NewRelicRequestMapper());
    }

    public GetRequest get(String url) {
        return Unirest
                .get(hostUrl + url)
                .header("X-Api-Key", apiKey)
                .header("accept", "application/json");
    }

    public HttpRequestWithBody put(String url) {
        return put(url, "application/json");
    }

    public HttpRequestWithBody put(String url, String contentType) {
        return Unirest
                .put(hostUrl + url)
                .header("X-Api-Key", apiKey)
                .header("accept", "application/json")
                .header("Content-Type", contentType);
    }

    public HttpRequestWithBody post(String url) {
        return post(url, "application/json");
    }

    public HttpRequestWithBody post(String url, String contentType) {
        return Unirest
                .post(hostUrl + url)
                .header("X-Api-Key", apiKey)
                .header("accept", "application/json")
                .header("Content-Type", contentType);
    }

    public HttpRequest delete(String url) {
        return Unirest
                .delete(hostUrl + url)
                .header("X-Api-Key", apiKey)
                .header("accept", "application/json");
    }

    public <T> T asObject(BaseRequest request, Class<T> responseType) throws NewRelicApiException {
        try {
            logRequest(request);
            HttpResponse<T> response = request.asObject(responseType);
            logResponse(request, response);
            return handleResponse(request, response);
        } catch (UnirestException e) {
            throw new NewRelicApiException(e);
        }
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

    private <T> T handleResponse(BaseRequest request, HttpResponse<T> response) throws NewRelicApiHttpException {
        if (200 <= response.getStatus() && response.getStatus() < 300) {
            return response.getBody();
        } else {
            throw new NewRelicApiHttpException(request, response);
        }
    }
}
