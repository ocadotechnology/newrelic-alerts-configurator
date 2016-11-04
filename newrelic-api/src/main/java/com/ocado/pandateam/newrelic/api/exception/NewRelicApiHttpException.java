package com.ocado.pandateam.newrelic.api.exception;

public class NewRelicApiHttpException extends NewRelicApiException {

    private final int statusCode;
    private final String message;
    private final String method;
    private final String url;

    public NewRelicApiHttpException(int statusCode, String message, String method, String url) {
        super(formatMessage(statusCode, message, method, url));
        this.statusCode = statusCode;
        this.message = message;
        this.method = method;
        this.url = url;
    }

    public NewRelicApiHttpException(int statusCode, String message, String method, String url, Throwable cause) {
        super(formatMessage(statusCode, message, method, url), cause);
        this.statusCode = statusCode;
        this.message = message;
        this.method = method;
        this.url = url;
    }

    private static String formatMessage(int statusCode, String message, String method, String url) {
        return String.format("NewRelic API returned error code %d: %s; %s %s", statusCode, message, method, url);
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

}
