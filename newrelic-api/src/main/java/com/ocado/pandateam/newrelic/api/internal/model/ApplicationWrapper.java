package com.ocado.pandateam.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.pandateam.newrelic.api.model.applications.Application;
import lombok.Value;

@Value
public class ApplicationWrapper {
    @JsonProperty
    Application application;
}
