package com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.mutation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Identifiable;
import lombok.Getter;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.NONE;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Getter(NONE)
public class NotificationChannelsResult implements MutationResult<List<Integer>> {
    List<Identifiable> notificationChannels;
    List<Map<String, Object>> errors;

    @Override
    public List<Integer> get() {
        return Optional.ofNullable(notificationChannels).orElseGet(ArrayList::new).stream()
                .map(Identifiable::getId).collect(toList());
    }

    @Override
    public List<Map<String, Object>> getErrors() {
        return errors;
    }
}
