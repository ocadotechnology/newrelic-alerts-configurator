package com.ocadotechnology.newrelic.alertconfiguploader.internal.entities;


import com.ocadotechnology.newrelic.api.NewRelicApi;

public interface IdProvider {
    Integer getId(NewRelicApi api, String name);
}
