package com.ocado.panda.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.panda.newrelic.api.model.ObjectList;
import com.ocado.panda.newrelic.api.model.servers.Server;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class ServerList extends ObjectList<Server> {
    @JsonProperty("servers")
    List<Server> list;

    @Override
    public ServerList merge(ObjectList<Server> list) {
        return new ServerList(mergeList(list));
    }
}
