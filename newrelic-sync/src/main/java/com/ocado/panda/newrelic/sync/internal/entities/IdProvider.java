package com.ocado.panda.newrelic.sync.internal.entities;


import com.ocado.newrelic.api.NewRelicApi;

public interface IdProvider {
    Integer getId(NewRelicApi api, String name);
}
