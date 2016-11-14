package com.ocado.pandateam.newrelic.api.exception;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.request.BaseRequest;

public class NewRelicApiHttpException extends NewRelicApiException {

    private final String method;
    private final String url;
    private final int statusCode;
    private final String statusText;

    public NewRelicApiHttpException(BaseRequest request, HttpResponse response) {
        super("");
        this.method = request.getHttpRequest().getHttpMethod().name();
        this.url = request.getHttpRequest().getUrl();
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
