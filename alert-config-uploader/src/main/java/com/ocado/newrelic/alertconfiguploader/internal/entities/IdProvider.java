package com.ocado.newrelic.alertconfiguploader.internal.entities;


import com.ocado.newrelic.api.NewRelicApi;

public interface IdProvider {
    Integer getId(NewRelicApi api, String name);
}
