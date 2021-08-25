package com.ocadotechnology.newrelic.apiclient.internal;

import com.ocadotechnology.newrelic.apiclient.internal.client.NewRelicClient;
import com.ocadotechnology.newrelic.apiclient.internal.model.MonitorList;
import com.ocadotechnology.newrelic.apiclient.model.synthetics.Monitor;
import static java.util.Collections.singletonList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class DefaultSyntheticsMonitorsApiTest {
    private Response postResponseMock;
    private Response getResponseMock;
    private NewRelicClient clientMock;
    private WebTarget webTargetMock;
    private Invocation.Builder builderMock;
    private DefaultSyntheticsMonitorsApi syntheticsMonitorsApi;

    @Before
    public void setUp() {
        postResponseMock = mock(Response.class);
        getResponseMock = mock(Response.class);
        clientMock = mock(NewRelicClient.class);
        webTargetMock = mock(WebTarget.class);
        builderMock = mock(Invocation.Builder.class);
        syntheticsMonitorsApi = new DefaultSyntheticsMonitorsApi(clientMock);
    }

    @Test
    public void create_shouldCreateANewMonitorAndReturnDetails() throws Exception {
        final Monitor expectedMonitorObject = new Monitor(UUID.randomUUID().toString(), "test-monitor", "SIMPLE", 1, singletonList("AWS_US_WEST_1"), "http://newrelic-url", "ENABLED", 1.0, null, null);;
        final Monitor actualMonitorToBeCreated = new Monitor("", "test-monitor", "SIMPLE", 1, singletonList("AWS_US_WEST_1"), "http://newrelic-url", "ENABLED", 1.0, null, null);
        final MonitorList monitorList = new MonitorList(Collections.singletonList(expectedMonitorObject));

        when(postResponseMock.getStatus()).thenReturn(201);
        when(builderMock.post(Entity.entity(actualMonitorToBeCreated, APPLICATION_JSON_TYPE))).thenReturn(postResponseMock);
        when(getResponseMock.readEntity(MonitorList.class)).thenReturn(monitorList);
        when(builderMock.get()).thenReturn(getResponseMock);
        when(webTargetMock.request(APPLICATION_JSON_TYPE)).thenReturn(builderMock);
        when(webTargetMock.request()).thenReturn(builderMock);
        when(clientMock.target("/v3/monitors")).thenReturn(webTargetMock).thenReturn(webTargetMock);

        final Monitor monitor = syntheticsMonitorsApi.create(actualMonitorToBeCreated);

        assertThat(monitor).isEqualTo(expectedMonitorObject);
    }

    @Test
    public void create_shouldThrowErrorIfFailedToCreateMonitor() {
        final Monitor actualMonitorToBeCreated = new Monitor("", "test-monitor", "SIMPLE", 1, singletonList("AWS_US_WEST_1"), "http://newrelic-url", "ENABLED", 1.0, null, null);

        when(postResponseMock.getStatus()).thenReturn(400);
        when(builderMock.post(Entity.entity(actualMonitorToBeCreated, APPLICATION_JSON_TYPE))).thenReturn(postResponseMock);
        when(webTargetMock.request(APPLICATION_JSON_TYPE)).thenReturn(builderMock);
        when(clientMock.target("/v3/monitors")).thenReturn(webTargetMock);

        assertThatThrownBy(() -> syntheticsMonitorsApi.create(actualMonitorToBeCreated)).isInstanceOf(Exception.class);
    }

    @Test
    public void delete_shouldDeleteTheMonitor() {
        final Monitor actualMonitorToBeDeleted = new Monitor(UUID.randomUUID().toString(), "test-monitor", "SIMPLE", 1, singletonList("AWS_US_WEST_1"), "http://newrelic-url", "ENABLED", 1.0, null, null);
        when(builderMock.delete()).thenReturn(postResponseMock);
        when(webTargetMock.request(APPLICATION_JSON_TYPE)).thenReturn(builderMock);
        when(clientMock.target("/v3/monitors/"+actualMonitorToBeDeleted.getUuid())).thenReturn(webTargetMock);

        final Monitor monitor = syntheticsMonitorsApi.delete(actualMonitorToBeDeleted);

        assertThat(monitor).isEqualTo(actualMonitorToBeDeleted);
    }
}
