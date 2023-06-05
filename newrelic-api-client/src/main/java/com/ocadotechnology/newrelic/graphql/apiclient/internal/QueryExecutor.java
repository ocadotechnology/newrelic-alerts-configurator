package com.ocadotechnology.newrelic.graphql.apiclient.internal;

import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Query;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Result;

public interface QueryExecutor {
    Result execute(Query query);
}
