package com.ocado.pandateam.newrelic.api;

import com.ocado.pandateam.newrelic.api.internal.NewRelicRestClient;

abstract class BaseApi {

    protected final NewRelicRestClient api;

    BaseApi(NewRelicRestClient api) {
        this.api = api;
    }
}
