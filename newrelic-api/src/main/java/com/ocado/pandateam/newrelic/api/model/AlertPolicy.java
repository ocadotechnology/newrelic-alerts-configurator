package com.ocado.pandateam.newrelic.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class AlertPolicy {
    @JsonProperty
    Integer id;
    @JsonProperty("incident_preference")
    String incidentPreference;
    @JsonProperty
    String name;
    @JsonProperty("created_at")
    Long createdAt;
    @JsonProperty("updated_at")
    Long updatedAt;
}
