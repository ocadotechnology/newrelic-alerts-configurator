package com.ocadotechnology.newrelic.alertsconfigurator.internal.entities;


import com.ocadotechnology.newrelic.apiclient.NewRelicApi;

public interface UuidProvider {
    String getUuid(NewRelicApi api, String name);
}
