package com.ocado.panda.newrelic.api.internal;


import com.ocado.panda.newrelic.api.ServersApi;
import com.ocado.panda.newrelic.api.internal.client.NewRelicClient;
import com.ocado.panda.newrelic.api.internal.model.ServerList;
import com.ocado.panda.newrelic.api.internal.model.ServerWrapper;
import com.ocado.panda.newrelic.api.model.servers.Server;
import org.glassfish.jersey.uri.UriComponent;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        ServerList serverList = client
                .target(SERVERS_URL)
                .queryParam("filter[name]", serverNameEncoded)
                .request(APPLICATION_JSON_TYPE)
                .get(ServerList.class);

        List<Server> servers = serverList.getList().stream()
                .filter(application -> application.getName().equals(serverName))
                .collect(Collectors.toList());

        serverList = new ServerList(servers);
        return serverList.getSingle();
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
