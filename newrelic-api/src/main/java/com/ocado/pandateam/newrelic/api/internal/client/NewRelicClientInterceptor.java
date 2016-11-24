package com.ocado.pandateam.newrelic.api.internal.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
class NewRelicClientInterceptor implements ClientRequestFilter, ClientResponseFilter {

    private static final String X_API_KEY_HEADER = "X-Api-Key";

    private final String apiKey;

    NewRelicClientInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void filter(ClientRequestContext request) throws IOException {
        request.getHeaders().putSingle(X_API_KEY_HEADER, apiKey);
        logRequest(request);
    }

    @Override
    public void filter(ClientRequestContext request, ClientResponseContext response) throws IOException {
        logResponse(request, response);
        if (!isSuccess(response)) {
            logResponseBody(response);
            throw new NewRelicClientException(request, response);
        }
    }

    private void logRequest(ClientRequestContext request) {
        LOG.info("{} {}",
                request.getMethod(),
                request.getUri());
    }

    private void logResponse(ClientRequestContext request, ClientResponseContext response) {
        LOG.info("{} {}: {} {}",
                request.getMethod(),
                request.getUri(),
                response.getStatus(),
                response.getStatusInfo().getReasonPhrase());
    }

    private void logResponseBody(ClientResponseContext response) {
        try {
            LOG.info("{}", IOUtils.toString(response.getEntityStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOG.error("Failed to log response body", e);
        }
    }

    private boolean isSuccess(ClientResponseContext response) {
        return 200 <= response.getStatus() && response.getStatus() < 300;
    }
}
