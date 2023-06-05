package com.ocadotechnology.newrelic.graphql.apiclient.internal.transport;

import java.util.List;

public interface Page<T extends List> {
    String getNextCursor();

    T getContent();
}
