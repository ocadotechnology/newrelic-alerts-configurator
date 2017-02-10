package com.ocado.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.newrelic.api.model.servers.Server;
import com.ocado.newrelic.api.model.ObjectList;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class ServerList extends ObjectList<Server, ServerList> {
    @JsonCreator
    public ServerList(@JsonProperty("servers") List<Server> items) {
        super(items, ServerList::new);
    }
}
