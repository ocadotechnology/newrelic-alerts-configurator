package com.ocadotechnology.newrelic.apiclient.model.synthetics;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

/**
 * See <a href="https://docs.newrelic.com/docs/apis/synthetics-rest-api/monitor-examples/manage-synthetics-monitors-rest-api">Doc</a>
 */
@Value
@Builder
@AllArgsConstructor
public class Monitor {
    @JsonProperty("id")
    String uuid;
    @JsonProperty
    String name;
    @JsonProperty
    String type;
    @JsonProperty
    Integer frequency;
    @JsonProperty
    @Singular
    Collection<String> locations;
    @JsonProperty
    String uri;
    @JsonProperty
    String status;
    @JsonProperty
    Double slaThreshold;
    @JsonProperty
    Integer userId;
    @JsonProperty
    String apiVersion;
}
