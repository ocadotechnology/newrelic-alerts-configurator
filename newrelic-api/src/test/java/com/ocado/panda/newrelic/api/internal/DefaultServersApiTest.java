package com.ocado.panda.newrelic.api.internal;

import com.ocado.panda.newrelic.api.ServersApi;
import com.ocado.panda.newrelic.api.internal.client.NewRelicClient;
import com.ocado.panda.newrelic.api.internal.model.ServerList;
import com.ocado.panda.newrelic.api.model.servers.Server;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
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
    private Invocation.Builder builderMock;

    private ServersApi testee;

    @Before
    public void setUp() {
        NewRelicClient clientMock = mock(NewRelicClient.class);
        WebTarget webTargetMock = mock(WebTarget.class);

        when(clientMock.target("/v2/servers.json")).thenReturn(webTargetMock);
        when(webTargetMock.queryParam("filter[name]", "server")).thenReturn(webTargetMock);
        when(webTargetMock.request(APPLICATION_JSON_TYPE)).thenReturn(builderMock);

        testee = new DefaultServersApi(clientMock);
    }

    @Test
    public void shouldReturnServerWhenClientReturnsNotUniqueResult() throws Exception {

        // given
        when(builderMock.get(ServerList.class)).thenReturn(new ServerList(asList(
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
        when(builderMock.get(ServerList.class)).thenReturn(new ServerList(Collections.singletonList(
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
        when(builderMock.get(ServerList.class)).thenReturn(new ServerList(Collections.emptyList()));

        // when
        Optional<Server> serverOptional = testee.getByName("server");

        // then
        assertThat(serverOptional).isEmpty();
    }
}
