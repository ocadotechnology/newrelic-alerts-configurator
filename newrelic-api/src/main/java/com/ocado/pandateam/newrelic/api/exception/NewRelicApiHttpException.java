package com.ocado.pandateam.newrelic.api.exception;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.request.HttpRequest;

public class NewRelicApiHttpException extends NewRelicApiException {

    private final String method;
    private final String url;
    private final int statusCode;
    private final String statusText;

    public NewRelicApiHttpException(HttpRequest request, HttpResponse response) {
        super("");
        this.method = request.getHttpMethod().name();
        this.url = request.getUrl();
        this.statusCode = response.getStatus();
        this.statusText = response.getStatusText();
    }

    @Override
    public String getMessage() {
        return String.format("NewRelic error %s: %s: %d %s", method, url, statusCode, statusText);
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }
}
