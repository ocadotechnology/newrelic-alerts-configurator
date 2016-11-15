package com.ocado.pandateam.newrelic.api.model.conditions.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.pandateam.newrelic.api.model.conditions.Terms;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class ExternalServiceCondition {
    Integer id;
    String type;
    String name;
    boolean enabled;
    Integer[] entities;
    String metric;
    @JsonProperty("runbook_url")
    String runbookUrl;
    @JsonProperty("external_service_url")
    String externalServiceUrl;
    Terms[] terms;
}
