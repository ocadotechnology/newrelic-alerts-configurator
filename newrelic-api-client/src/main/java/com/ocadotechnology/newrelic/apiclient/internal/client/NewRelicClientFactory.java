package com.ocadotechnology.newrelic.apiclient.internal.client;

public class NewRelicClientFactory {
    protected final String apiKey;

    public NewRelicClientFactory(String apiKey) {
        if(apiKey == null) {
            throw new NullPointerException("apiKey must be provided");
        }
        this.apiKey = apiKey;
    }

    public NewRelicClient create(String hostUrl) {
        return new DefaultNewRelicClient(hostUrl, apiKey);
    }
}
