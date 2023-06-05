package com.ocadotechnology.newrelic.graphql.apiclient.internal.transport;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import static com.ocadotechnology.newrelic.graphql.apiclient.internal.util.Preconditions.checkArgument;
import static java.lang.String.format;
import static lombok.AccessLevel.PACKAGE;

@Value
@RequiredArgsConstructor(access = PACKAGE)
public class Query {

    public String query;

    public static Query of(String query) {
        checkArgument(query != null, "Query must not be null");
        return new Query(query);
    }

    public static Query wrapped(String query) {
        checkArgument(query != null, "Query must not be null");
        return new Query(format("{    %s    }", query));
    }
}