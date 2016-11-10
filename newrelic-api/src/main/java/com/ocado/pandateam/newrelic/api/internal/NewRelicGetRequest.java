package com.ocado.pandateam.newrelic.api.internal;

import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NewRelicGetRequest extends NewRelicRequest {

    private final HttpRequest getRequest;

    NewRelicGetRequest(GetRequest getRequest) {
        super(getRequest);
        this.getRequest = getRequest;
    }

    public NewRelicGetRequest queryString(String name, Object value) {
        getRequest.queryString(name, value);
        return this;
    }

    public NewRelicGetRequest routeParam(String name, Object value) {
        getRequest.routeParam(name, String.valueOf(value));
        return this;
    }

    @Override
    protected void logRequest() {
        log.info("{} {}", getRequest.getHttpMethod(), getRequest.getUrl());
    }
}
