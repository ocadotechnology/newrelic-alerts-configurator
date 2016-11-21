package com.ocado.pandateam.newrelic.api.internal;

abstract class ApiBase {

    protected final NewRelicPageableClient api;

    ApiBase(NewRelicPageableClient api) {
        this.api = api;
    }
}
