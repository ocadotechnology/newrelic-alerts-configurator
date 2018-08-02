package com.ocadotechnology.newrelic.apiclient.internal;

import java.util.Optional;

import javax.ws.rs.client.Invocation;

import com.ocadotechnology.newrelic.apiclient.SyntheticsMonitorsApi;
import com.ocadotechnology.newrelic.apiclient.internal.client.NewRelicClient;
import com.ocadotechnology.newrelic.apiclient.internal.model.MonitorList;
import com.ocadotechnology.newrelic.apiclient.model.synthetics.Monitor;

class DefaultSyntheticsMonitorsApi extends ApiBase implements SyntheticsMonitorsApi {

    private static final String MONITORS_URL = "/v3/monitors";

    DefaultSyntheticsMonitorsApi(NewRelicClient client) {
        super(client);
    }

    @Override
    public Optional<Monitor> getByName(String monitorName) {
        Invocation.Builder builder = client
                .target(MONITORS_URL)
                .request();
        return getPageable(builder, MonitorList.class)
                .filter(monitor -> monitor.getName().equals(monitorName))
                .getSingle();
    }

}
