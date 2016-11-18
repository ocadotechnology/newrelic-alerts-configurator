package com.ocado.pandateam.newrelic.api.exception;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.request.BaseRequest;

public class NewRelicApiHttpException extends NewRelicApiException {

    public NewRelicApiHttpException(BaseRequest request, HttpResponse response) {
        super(formatMessage(request, response));

    }

    private static String formatMessage(BaseRequest request, HttpResponse response) {
        String method = request.getHttpRequest().getHttpMethod().name();
        String url = request.getHttpRequest().getUrl();
        int statusCode = response.getStatus();
        String statusText = response.getStatusText();
        return String.format("%s %s: %d %s", method, url, statusCode, statusText);
    }
}
