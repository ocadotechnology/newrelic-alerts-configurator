package com.ocadotechnology.newrelic.graphql.apiclient;

public class GraphqlApiError extends RuntimeException {
    public GraphqlApiError(String message) {
        super(message);
    }

    public GraphqlApiError(Throwable cause) {
        super(cause);
    }
}
