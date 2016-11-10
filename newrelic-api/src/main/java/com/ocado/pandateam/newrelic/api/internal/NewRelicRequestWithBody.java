package com.ocado.pandateam.newrelic.api.internal;

import com.mashape.unirest.request.HttpRequestWithBody;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NewRelicRequestWithBody extends NewRelicRequest {

    private final HttpRequestWithBody requestWithBody;

    private Object body;

    NewRelicRequestWithBody(HttpRequestWithBody requestWithBody) {
        super(requestWithBody);
        this.requestWithBody = requestWithBody;
    }

    public NewRelicRequestWithBody body(Object body) {
        this.body = body;
        requestWithBody.header("Content-Type", "application/json");
        requestWithBody.body(body);
        return this;
    }

    public NewRelicRequestWithBody routeParam(String name, Object value) {
        requestWithBody.routeParam(name, String.valueOf(value));
        return this;
    }

    @Override
    protected void logRequest() {
        log.info("{} {}", requestWithBody.getHttpMethod(), requestWithBody.getUrl());
        log.info("{}", body);
    }
}
