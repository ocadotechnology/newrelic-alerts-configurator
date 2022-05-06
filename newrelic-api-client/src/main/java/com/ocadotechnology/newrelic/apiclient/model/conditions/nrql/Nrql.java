package com.ocadotechnology.newrelic.apiclient.model.conditions.nrql;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Value
@Builder
@AllArgsConstructor
public class Nrql {
    @JsonProperty
    String query;
    @JsonInclude(NON_NULL)
    @JsonProperty("since_value")
    String sinceValue;
}
