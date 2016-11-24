package com.ocado.pandateam.newrelic.api.internal.jersey;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;

public class NewRelicClientException extends ProcessingException {

    public NewRelicClientException(ClientRequestContext request, ClientResponseContext response) {
        super(formatMessage(request, response));

    }

    private static String formatMessage(ClientRequestContext request, ClientResponseContext response) {
        String method = request.getMethod();
        String url = request.getUri().toString();
        int statusCode = response.getStatus();
        String statusText = response.getStatusInfo().getReasonPhrase();
        return String.format("%s %s: %d %s", method, url, statusCode, statusText);
    }
}
