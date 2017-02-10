package com.ocadotechnology.newrelic.api.internal;

import com.ocadotechnology.newrelic.api.ApplicationsApi;
import com.ocadotechnology.newrelic.api.internal.DefaultApplicationsApi;
import com.ocadotechnology.newrelic.api.internal.client.NewRelicClient;
import com.ocadotechnology.newrelic.api.internal.model.ApplicationList;
import com.ocadotechnology.newrelic.api.model.applications.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Optional;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultApplicationsApiTest {
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
    public void shouldReturnApplicationWhenClientReturnsNotUniqueResult() throws Exception {

        // given
        when(responseMock.readEntity(ApplicationList.class)).thenReturn(new ApplicationList(asList(
                Application.builder().name("app").build(),
                Application.builder().name("app1").build()
        )));

        // when
        Optional<Application> applicationOptional = testee.getByName("app");

        // then
        assertTrue(applicationOptional.isPresent());
    }

    @Test
    public void shouldNotReturnApplicationWhenClientReturnsNotMatchingResult() throws Exception {

        // given
        when(responseMock.readEntity(ApplicationList.class)).thenReturn(new ApplicationList(Collections.singletonList(
                Application.builder().name("app1").build()
        )));

        // when
        Optional<Application> applicationOptional = testee.getByName("app");

        // then
        assertFalse(applicationOptional.isPresent());
    }

    @Test
    public void shouldNotReturnApplicationWhenClientReturnsEmptyList() throws Exception {

        // given
        when(responseMock.readEntity(ApplicationList.class)).thenReturn(new ApplicationList(Collections.emptyList()));

        // when
        Optional<Application> applicationOptional = testee.getByName("app");

        // then
        assertFalse(applicationOptional.isPresent());
    }
}
