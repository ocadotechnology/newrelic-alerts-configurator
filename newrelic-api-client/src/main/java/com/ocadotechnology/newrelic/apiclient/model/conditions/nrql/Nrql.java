package com.ocadotechnology.newrelic.apiclient.model.conditions.nrql;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Nrql {
    @JsonProperty
    String query;
}
