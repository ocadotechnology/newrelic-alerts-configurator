package com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.mutation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.NotificationChannel;
import lombok.Getter;
import lombok.Value;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static lombok.AccessLevel.NONE;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Getter(NONE)
public class NotificationChannelResult implements MutationResult<NotificationChannel> {
    NotificationChannel notificationChannel;
    Map<String, Object> error;

    @Override
    public NotificationChannel get() {
        return notificationChannel;
    }

    @Override
    public List<Map<String, Object>> getErrors() {
        return Optional.ofNullable(error).map(e -> singletonList(error)).orElse(null);
    }
}
