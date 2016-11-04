package com.ocado.pandateam.newrelic.api.exception;

public class NewRelicApiHttpException extends NewRelicApiException {

    private final int statusCode;
    private final String statusText;
    private final String method;
    private final String url;

    public NewRelicApiHttpException(int statusCode, String statusText, String method, String url, String body) {
        super(formatMessage(statusCode, statusText, method, url, body));
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.method = method;
        this.url = url;
    }

    private static String formatMessage(int statusCode, String statusText, String method, String url, String body) {
        return String.format(
                "NewRelic API returned error code %d: %s; %s %s; body: %s",
                statusCode, statusText, method, url, body);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

}
