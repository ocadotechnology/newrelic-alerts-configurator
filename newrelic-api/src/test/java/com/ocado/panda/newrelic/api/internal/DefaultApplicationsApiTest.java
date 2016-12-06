package com.ocado.panda.newrelic.api.internal;

import com.ocado.panda.newrelic.api.ApplicationsApi;
import com.ocado.panda.newrelic.api.internal.client.NewRelicClient;
import com.ocado.panda.newrelic.api.internal.model.ApplicationList;
import com.ocado.panda.newrelic.api.model.applications.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
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
    private Builder builderMock;

    private ApplicationsApi testee;

    @Before
    public void setUp() {
        NewRelicClient clientMock = mock(NewRelicClient.class);
        WebTarget webTargetMock = mock(WebTarget.class);

        when(clientMock.target("/v2/applications.json")).thenReturn(webTargetMock);
        when(webTargetMock.queryParam("filter[name]", "app")).thenReturn(webTargetMock);
        when(webTargetMock.request(APPLICATION_JSON_TYPE)).thenReturn(builderMock);

        testee = new DefaultApplicationsApi(clientMock);
    }

    @Test
    public void shouldReturnApplicationWhenClientReturnsNotUniqueResult() throws Exception {

        // given
        when(builderMock.get(ApplicationList.class)).thenReturn(new ApplicationList(asList(
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
        when(builderMock.get(ApplicationList.class)).thenReturn(new ApplicationList(Collections.singletonList(
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
        when(builderMock.get(ApplicationList.class)).thenReturn(new ApplicationList(Collections.emptyList()));

        // when
        Optional<Application> applicationOptional = testee.getByName("app");

        // then
        assertFalse(applicationOptional.isPresent());
    }
}