package com.ocadotechnology.newrelic.graphql.apiclient.internal.transport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
public class AssociatedPolicies {
    List<Identifiable> policies;
}
