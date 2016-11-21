package com.ocado.pandateam.newrelic.sync.configuration;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class ApplicationConfiguration {
    @NonNull
    private String applicationName;
    private float appApdexThreshold;
    private float endUserApdexThreshold;
    private boolean enableRealUserMonitoring;
}
