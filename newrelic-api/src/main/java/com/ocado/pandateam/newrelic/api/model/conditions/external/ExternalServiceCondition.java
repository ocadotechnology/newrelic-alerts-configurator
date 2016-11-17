package com.ocado.pandateam.newrelic.api.model.conditions.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.pandateam.newrelic.api.model.conditions.Terms;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Collection;

@Value
@Builder
@AllArgsConstructor
public class ExternalServiceCondition {
    @JsonProperty
    Integer id;
    @JsonProperty
    String type;
    @JsonProperty
    String name;
    @JsonProperty
    Boolean enabled;
    @JsonProperty
    Collection<Integer> entities;
    @JsonProperty
    String metric;
    @JsonProperty("runbook_url")
    String runbookUrl;
    @JsonProperty("external_service_url")
    String externalServiceUrl;
    @JsonProperty
    Collection<Terms> terms;
}
