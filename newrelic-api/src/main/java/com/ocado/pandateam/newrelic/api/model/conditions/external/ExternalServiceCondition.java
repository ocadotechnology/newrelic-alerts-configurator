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
    Integer id;
    String type;
    String name;
    boolean enabled;
    Collection<Integer> entities;
    String metric;
    @JsonProperty("runbook_url")
    String runbookUrl;
    @JsonProperty("external_service_url")
    String externalServiceUrl;
    Collection<Terms> terms;
}
