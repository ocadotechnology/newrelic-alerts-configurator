package com.ocadotechnology.newrelic.alertconfiguploader.internal.entities;


import com.ocadotechnology.newrelic.alertconfiguploader.exception.NewRelicSyncException;
import com.ocadotechnology.newrelic.api.NewRelicApi;
import com.ocadotechnology.newrelic.api.model.servers.Server;

import java.util.Optional;

import static java.lang.String.format;

class ServerIdProvider implements IdProvider {
    @Override
    public Integer getId(NewRelicApi api, String name) {
        Optional<Server> serverOptional = api.getServersApi().getByName(name);
        Server server = serverOptional.orElseThrow(
                () -> new NewRelicSyncException(format("Server %s does not exist", name)));
        return server.getId();
    }
}
