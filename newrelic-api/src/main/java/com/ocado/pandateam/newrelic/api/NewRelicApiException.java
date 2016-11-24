package com.ocado.pandateam.newrelic.api;

public class NewRelicApiException extends RuntimeException {

    public NewRelicApiException(String message) {
        super(message);
    }

    public NewRelicApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public NewRelicApiException(Throwable cause) {
        super(cause);
    }

}
