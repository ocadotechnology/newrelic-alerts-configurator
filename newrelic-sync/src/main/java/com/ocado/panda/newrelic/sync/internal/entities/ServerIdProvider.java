package com.ocado.panda.newrelic.sync.internal.entities;


import com.ocado.newrelic.api.NewRelicApi;
import com.ocado.newrelic.api.model.servers.Server;
import com.ocado.panda.newrelic.sync.exception.NewRelicSyncException;

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
