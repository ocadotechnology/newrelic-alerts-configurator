package com.ocado.pandateam.newrelic.api.internal;

import com.mashape.unirest.http.Unirest;

public class NewRelicRestClient {

    private final String hostUrl;
    private final String apiKey;

    public NewRelicRestClient(String hostUrl, String apiKey) {
        this.hostUrl = hostUrl;
        this.apiKey = apiKey;
        Unirest.setObjectMapper(new NewRelicRequestMapper());
    }

    public NewRelicGetRequest get(String url) {
        return new NewRelicGetRequest(Unirest.get(hostUrl + url).header("X-Api-Key", apiKey));
    }

    public NewRelicRequestWithBody post(String url) {
        return new NewRelicRequestWithBody(Unirest.post(hostUrl + url).header("X-Api-Key", apiKey));
    }

    public NewRelicRequestWithBody put(String url) {
        return new NewRelicRequestWithBody(Unirest.put(hostUrl + url).header("X-Api-Key", apiKey));
    }
}
