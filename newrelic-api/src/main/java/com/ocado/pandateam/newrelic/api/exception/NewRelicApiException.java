package com.ocado.pandateam.newrelic.api.exception;

public class NewRelicApiException extends Exception {

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
