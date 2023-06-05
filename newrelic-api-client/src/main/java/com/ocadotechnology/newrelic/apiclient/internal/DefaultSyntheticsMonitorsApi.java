package com.ocadotechnology.newrelic.apiclient.internal;

import com.ocadotechnology.newrelic.apiclient.SyntheticsMonitorsApi;
import com.ocadotechnology.newrelic.apiclient.internal.client.NewRelicClient;
import com.ocadotechnology.newrelic.apiclient.internal.model.MonitorList;
import com.ocadotechnology.newrelic.apiclient.model.synthetics.Monitor;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class DefaultSyntheticsMonitorsApi extends ApiBase implements SyntheticsMonitorsApi {

    private static final String MONITORS_URL = "/v3/monitors";

    public DefaultSyntheticsMonitorsApi(NewRelicClient client) {
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

    @Override
    public Monitor create(Monitor monitor) throws Exception {
        final Response response = client
                .target(MONITORS_URL)
                .request(APPLICATION_JSON_TYPE)
                .post(Entity.entity(monitor, APPLICATION_JSON_TYPE));

        if(response.getStatus() != 201) {
            throw new Exception(String.format("Failed to create the Monitor with name %s", monitor.getName()));
        }
        return getByName(monitor.getName()).get();
    }

    @Override
    public Monitor delete(Monitor monitor) {
        client
                .target(MONITORS_URL + "/" + monitor.getUuid())
                .request(APPLICATION_JSON_TYPE)
                .delete();
        return monitor;
    }
}
