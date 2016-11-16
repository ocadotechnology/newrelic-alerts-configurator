package com.ocado.pandateam.newrelic.api.model.applications;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ApplicationInputWrapper {
    @JsonProperty
    ApplicationInput application;
}
