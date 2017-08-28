package com.ocadotechnology.newrelic.api;

import com.ocadotechnology.newrelic.api.model.servers.Server;

import java.util.Optional;

public interface ServersApi {
    /**
     * Gets {@link Server} object using its name.
     *
     * @param serverName name of the server registered in NewRelic
     * @return Optional containing {@link Server} object, or empty if server not found
     */
    Optional<Server> getByName(String serverName);

    /**
     * Gets {@link Server} object using its id.
     *
     * @param serverId id of the server registered in NewRelic
     * @return found {@link Server}
     */
    Server getById(int serverId);
}
