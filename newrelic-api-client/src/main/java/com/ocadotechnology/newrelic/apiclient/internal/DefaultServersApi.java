package com.ocadotechnology.newrelic.apiclient.internal;


import com.ocadotechnology.newrelic.apiclient.ServersApi;
import com.ocadotechnology.newrelic.apiclient.internal.client.NewRelicClient;
import com.ocadotechnology.newrelic.apiclient.internal.model.ServerList;
import com.ocadotechnology.newrelic.apiclient.internal.model.ServerWrapper;
import com.ocadotechnology.newrelic.apiclient.model.servers.Server;
import org.glassfish.jersey.uri.UriComponent;

import javax.ws.rs.client.Invocation;
import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.glassfish.jersey.uri.UriComponent.Type.QUERY_PARAM_SPACE_ENCODED;

class DefaultServersApi extends ApiBase implements ServersApi {
    private static final String SERVERS_URL = "/v2/servers.json";
    private static final String SERVER_URL = "/v2/servers/{server_id}.json";

    DefaultServersApi(NewRelicClient client) {
        super(client);
    }

    @Override
    public Optional<Server> getByName(String serverName) {
        String serverNameEncoded = UriComponent.encode(serverName, QUERY_PARAM_SPACE_ENCODED);
        Invocation.Builder builder = client
                .target(SERVERS_URL)
                .queryParam("filter[name]", serverNameEncoded)
                .request(APPLICATION_JSON_TYPE);
        return getPageable(builder, ServerList.class)
                .filter(application -> application.getName().equals(serverName))
                .getSingle();
    }

    @Override
    public Server getById(int serverId) {
        return client
                .target(SERVER_URL)
                .resolveTemplate("server_id", serverId)
                .request(APPLICATION_JSON_TYPE)
                .get(ServerWrapper.class)
                .getServer();
    }
}
