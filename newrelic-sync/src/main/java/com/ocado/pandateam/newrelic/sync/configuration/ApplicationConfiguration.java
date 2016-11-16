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

    public ApplicationConfiguration(String applicationName, float appApdexThreshold, float endUserApdexThreshold,
                                    boolean enableRealUserMonitoring) {
        this.applicationName = applicationName;
        this.appApdexThreshold = appApdexThreshold;
        this.endUserApdexThreshold = endUserApdexThreshold;
        this.enableRealUserMonitoring = enableRealUserMonitoring;
    }
}
