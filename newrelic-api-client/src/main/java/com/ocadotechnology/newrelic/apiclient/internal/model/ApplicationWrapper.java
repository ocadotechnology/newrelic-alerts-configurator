package com.ocadotechnology.newrelic.apiclient.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.apiclient.model.applications.Application;
import lombok.Value;

@Value
public class ApplicationWrapper {
    @JsonProperty
    Application application;
}
