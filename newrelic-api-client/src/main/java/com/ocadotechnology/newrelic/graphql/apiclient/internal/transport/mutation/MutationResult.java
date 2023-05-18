package com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.mutation;

import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

public interface MutationResult<T> {
    T get();

    List<Map<String, Object>> getErrors();

    default boolean hasErrors() {
        return nonNull(getErrors()) && !getErrors().isEmpty();
    }
}
