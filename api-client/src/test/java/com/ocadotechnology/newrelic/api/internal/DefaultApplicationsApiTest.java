package com.ocadotechnology.newrelic.api.internal;

import com.ocadotechnology.newrelic.api.ApplicationsApi;
import com.ocadotechnology.newrelic.api.internal.client.NewRelicClient;
import com.ocadotechnology.newrelic.api.internal.model.ApplicationList;
import com.ocadotechnology.newrelic.api.model.applications.Application;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Optional;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultApplicationsApiTest {
    @Rule
    public final MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    private Response responseMock;
    private ApplicationsApi testee;

    @Before
    public void setUp() {
        NewRelicClient clientMock = mock(NewRelicClient.class);
        WebTarget webTargetMock = mock(WebTarget.class);
        Invocation.Builder builderMock = mock(Invocation.Builder.class);

        when(clientMock.target("/v2/applications.json")).thenReturn(webTargetMock);
        when(webTargetMock.queryParam("filter[name]", "app")).thenReturn(webTargetMock);
        when(webTargetMock.request(APPLICATION_JSON_TYPE)).thenReturn(builderMock);
        when(builderMock.get()).thenReturn(responseMock);

        testee = new DefaultApplicationsApi(clientMock);
    }

    @Test
    public void getByName_shouldReturnApplication_whenClientReturnsNotUniqueResult() throws Exception {

        // given
        when(responseMock.readEntity(ApplicationList.class)).thenReturn(new ApplicationList(asList(
                Application.builder().name("app").build(),
                Application.builder().name("app1").build()
        )));

        // when
        Optional<Application> applicationOptional = testee.getByName("app");

        // then
        assertThat(applicationOptional).isNotEmpty();
    }

    @Test
    public void getByName_shouldNotReturnApplication_whenClientReturnsNotMatchingResult() throws Exception {

        // given
        when(responseMock.readEntity(ApplicationList.class)).thenReturn(new ApplicationList(Collections.singletonList(
                Application.builder().name("app1").build()
        )));

        // when
        Optional<Application> applicationOptional = testee.getByName("app");

        // then
        assertThat(applicationOptional).isEmpty();
    }

    @Test
    public void getByName_shouldNotReturnApplication_whenClientReturnsEmptyList() throws Exception {

        // given
        when(responseMock.readEntity(ApplicationList.class)).thenReturn(new ApplicationList(Collections.emptyList()));

        // when
        Optional<Application> applicationOptional = testee.getByName("app");

        // then
        assertThat(applicationOptional).isEmpty();
    }
}
