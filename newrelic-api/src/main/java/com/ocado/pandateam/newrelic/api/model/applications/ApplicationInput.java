package com.ocado.pandateam.newrelic.api.model.applications;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class ApplicationInput {
    @JsonProperty
    String name;
    @JsonProperty
    SettingsInput settings;
}
