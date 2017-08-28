package com.ocadotechnology.newrelic.alertsconfigurator.internal.entities;


import com.ocadotechnology.newrelic.apiclient.NewRelicApi;

public interface IdProvider {
    Integer getId(NewRelicApi api, String name);
}
