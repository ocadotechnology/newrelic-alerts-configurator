package com.ocadotechnology.newrelic.graphql.apiclient.internal.transport;

import lombok.Value;

@Value
public class HttpHeader {
    String name;
    String value;
}
