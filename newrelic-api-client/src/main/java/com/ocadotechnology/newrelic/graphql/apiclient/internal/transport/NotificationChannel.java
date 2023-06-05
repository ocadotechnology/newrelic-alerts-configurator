package com.ocadotechnology.newrelic.graphql.apiclient.internal.transport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
public class NotificationChannel {
    String id;
    String name;
    String type;
    AssociatedPolicies associatedPolicies;
    NotificationChannelConfig config;

}
