package com.ocadotechnology.newrelic.graphql.apiclient.internal.testutil;

import com.ocadotechnology.newrelic.graphql.apiclient.internal.QueryExecutor;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Query;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

public class QueryExecutorMock implements QueryExecutor {
    private final Map<Query, Result> resultsByQuery = new HashMap<>();

    @Override
    public Result execute(Query query) {
        return Optional.ofNullable(resultsByQuery.get(query))
                .orElseThrow(() -> new UnsupportedOperationException(format("No scenario found for query:'\n%s\n'", query)));
    }


    public QueryExecutorMock addScenario(Query query, Result result) {
        resultsByQuery.put(query, result);
        return this;
    }
}
