package com.ocadotechnology.newrelic.graphql.apiclient.internal;

import com.ocadotechnology.newrelic.apiclient.internal.client.NewRelicClient;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Query;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Result;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.client.Entity;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

@RequiredArgsConstructor
public class DefaultQueryExecutor implements QueryExecutor {
    private final NewRelicClient client;
    @Override
    public Result execute(Query query) {
        return client.target("")
                .request(APPLICATION_JSON_TYPE)
                .buildPost(Entity.entity(query, APPLICATION_JSON_TYPE))
                .invoke()
                .readEntity(Result.class);
    }
}
