package com.ocado.pandateam.newrelic.api.internal;

import com.mashape.unirest.request.HttpRequestWithBody;

public class NewRelicRequestWithBody extends NewRelicRequest {

    private final HttpRequestWithBody httpRequestWithBody;

    NewRelicRequestWithBody(HttpRequestWithBody httpRequest) {
        super(httpRequest);
        this.httpRequestWithBody = httpRequest;
    }

    public NewRelicRequestWithBody body(Object body) {
        httpRequestWithBody.header("Content-Type", "application/json");
        httpRequestWithBody.body(body);
        return this;
    }
}
