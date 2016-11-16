package com.ocado.pandateam.newrelic.sync.configuration;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApplicationConfiguration {
    private String applicationName;
    private float appApdexThreshold;
    private float userApdexThreshold;
}
