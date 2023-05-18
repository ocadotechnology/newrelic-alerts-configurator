package com.ocadotechnology.newrelic.graphql.apiclient.internal.util;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;


@NoArgsConstructor(access = PRIVATE)
public final class Preconditions {

    public static void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }
}
