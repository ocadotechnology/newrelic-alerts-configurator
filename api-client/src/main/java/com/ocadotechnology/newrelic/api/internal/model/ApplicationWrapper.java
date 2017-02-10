package com.ocadotechnology.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.api.model.applications.Application;
import lombok.Value;

@Value
public class ApplicationWrapper {
    @JsonProperty
    Application application;
}
