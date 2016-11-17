package com.ocado.pandateam.newrelic.sync.exception;

public class NewRelicSyncException extends RuntimeException {
    public NewRelicSyncException(String message) {
        super(message);
    }
}
