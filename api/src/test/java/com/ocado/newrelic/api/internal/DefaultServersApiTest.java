package com.ocado.newrelic.api.internal;

import com.ocado.newrelic.api.ServersApi;
import com.ocado.newrelic.api.internal.client.NewRelicClient;
import com.ocado.newrelic.api.internal.model.ServerList;
import com.ocado.newrelic.api.model.servers.Server;
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

public class DefaultServersApiTest {
    @Rule
    public final MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    private Response responseMock;
    private ServersApi testee;

    @Before
    public void setUp() {
        NewRelicClient clientMock = mock(NewRelicClient.class);
        WebTarget webTargetMock = mock(WebTarget.class);
        Invocation.Builder builderMock = mock(Invocation.Builder.class);

        when(clientMock.target("/v2/servers.json")).thenReturn(webTargetMock);
        when(webTargetMock.queryParam("filter[name]", "server")).thenReturn(webTargetMock);
        when(webTargetMock.request(APPLICATION_JSON_TYPE)).thenReturn(builderMock);
        when(builderMock.get()).thenReturn(responseMock);

        testee = new DefaultServersApi(clientMock);
    }

    @Test
    public void shouldReturnServerWhenClientReturnsNotUniqueResult() throws Exception {

        // given
        when(responseMock.readEntity(ServerList.class)).thenReturn(new ServerList(asList(
                Server.builder().name("server").build(),
                Server.builder().name("server1").build()
        )));

        // when
        Optional<Server> serverOptional = testee.getByName("server");

        // then
        assertThat(serverOptional).isNotEmpty();
    }

    @Test
    public void shouldNotReturnServerWhenClientReturnsNotMatchingResult() throws Exception {

        // given
        when(responseMock.readEntity(ServerList.class)).thenReturn(new ServerList(Collections.singletonList(
                Server.builder().name("server1").build()
        )));

        // when
        Optional<Server> serverOptional = testee.getByName("server");

        // then
        assertThat(serverOptional).isEmpty();
    }

    @Test
    public void shouldNotReturnServerWhenClientReturnsEmptyList() throws Exception {

        // given
        when(responseMock.readEntity(ServerList.class)).thenReturn(new ServerList(Collections.emptyList()));

        // when
        Optional<Server> serverOptional = testee.getByName("server");

        // then
        assertThat(serverOptional).isEmpty();
    }
}
