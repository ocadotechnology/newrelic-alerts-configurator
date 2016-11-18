package com.ocado.pandateam.newrelic.api.internal;

abstract class ApiBase {

    protected final NewRelicRestClient api;

    ApiBase(NewRelicRestClient api) {
        this.api = api;
    }
}
