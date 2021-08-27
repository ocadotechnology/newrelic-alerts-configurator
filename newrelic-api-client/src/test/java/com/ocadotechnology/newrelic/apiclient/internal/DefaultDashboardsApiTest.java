package com.ocadotechnology.newrelic.apiclient.internal;

import com.ocadotechnology.newrelic.apiclient.DashboardsApi;
import com.ocadotechnology.newrelic.apiclient.internal.client.NewRelicClient;
import com.ocadotechnology.newrelic.apiclient.internal.model.DashboardList;
import com.ocadotechnology.newrelic.apiclient.model.dashboards.Dashboard;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDashboardsApiTest {

    private static final String DASHBOARD_FILTER_VALUE = "dashboard";

    @Mock
    private Response responseMock;

    private DashboardsApi testee;

    @Before
    public void setUp() {
        NewRelicClient clientMock = mock(NewRelicClient.class);
        WebTarget webTargetMock = mock(WebTarget.class);
        Invocation.Builder builderMock = mock(Invocation.Builder.class);

        when(clientMock.target("/v2/dashboards.json")).thenReturn(webTargetMock);
        when(webTargetMock.queryParam("filter[title]", DASHBOARD_FILTER_VALUE)).thenReturn(webTargetMock);
        when(webTargetMock.request(APPLICATION_JSON_TYPE)).thenReturn(builderMock);
        when(builderMock.get()).thenReturn(responseMock);

        testee = new DefaultDashboardsApi(clientMock);
    }

    @Test
    public void getByTitle_shouldReturnDashboards_whenClientReturnsNotUniqueResult() {
        when(responseMock.readEntity(DashboardList.class)).thenReturn(new DashboardList(asList(
                Dashboard.builder().title(DASHBOARD_FILTER_VALUE).build(),
                Dashboard.builder().title("dashboard1").build()
        )));

        List<Dashboard> dashboard = testee.getByTitle(DASHBOARD_FILTER_VALUE);

        assertThat(dashboard).isNotEmpty();
        assertThat(dashboard).hasSize(2);
    }

    @Test
    public void getByTitle_shouldReturnDashboard_whenClientReturnsResultContainingQueriedTitle() {
        when(responseMock.readEntity(DashboardList.class)).thenReturn(new DashboardList(Collections.singletonList(
                Dashboard.builder().title("dashboard1").build()
        )));

        List<Dashboard> dashboard = testee.getByTitle(DASHBOARD_FILTER_VALUE);

        assertThat(dashboard).isNotEmpty();
    }

    @Test
    public void getByTitle_shouldNotReturnDashboard_whenClientReturnsEmptyList() {
        when(responseMock.readEntity(DashboardList.class)).thenReturn(new DashboardList(Collections.emptyList()));

        List<Dashboard> dashboard = testee.getByTitle(DASHBOARD_FILTER_VALUE);

        assertThat(dashboard).isEmpty();
    }
}
