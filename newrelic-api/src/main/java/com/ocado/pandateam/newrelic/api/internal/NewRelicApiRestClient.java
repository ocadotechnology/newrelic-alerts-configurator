package com.ocado.pandateam.newrelic.api.internal;

import com.mashape.unirest.http.Unirest;

public class NewRelicApiRestClient {

    private final String hostUrl;
    private final String apiKey;

    public NewRelicApiRestClient(String hostUrl, String apiKey) {
        this.hostUrl = hostUrl;
        this.apiKey = apiKey;
        Unirest.setObjectMapper(new Mapper());
    }

    public NewRelicRequest get(String url) {
        return new NewRelicRequest(Unirest.get(hostUrl + url).header("X-Api-Key", apiKey));
    }

    public NewRelicRequestWithBody post(String url) {
        return new NewRelicRequestWithBody(Unirest.post(hostUrl + url).header("X-Api-Key", apiKey));
    }

}
