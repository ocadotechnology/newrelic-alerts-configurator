package com.ocado.pandateam.newrelic.api;

import com.ocado.pandateam.newrelic.api.internal.NewRelicRestClient;

abstract class ApiBase {

    protected final NewRelicRestClient api;

    ApiBase(NewRelicRestClient api) {
        this.api = api;
    }
}
