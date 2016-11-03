package com.ocado.pandateam.newrelic.api.exception;

public class NewRelicApiRuntimeException extends RuntimeException {

    public NewRelicApiRuntimeException(String message) {
        super(message);
    }

    public NewRelicApiRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NewRelicApiRuntimeException(Throwable cause) {
        super(cause);
    }

}
