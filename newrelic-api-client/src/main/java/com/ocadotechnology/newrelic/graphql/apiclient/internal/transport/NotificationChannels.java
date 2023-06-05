package com.ocadotechnology.newrelic.graphql.apiclient.internal.transport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
public class NotificationChannels implements Page<List<NotificationChannel>> {
    List<NotificationChannel> channels;
    String nextCursor;

    @Override
    public List<NotificationChannel> getContent() {
        return getChannels();
    }
}
