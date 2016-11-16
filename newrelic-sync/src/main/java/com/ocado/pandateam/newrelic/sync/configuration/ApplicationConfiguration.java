package com.ocado.pandateam.newrelic.sync.configuration;

import lombok.Getter;

@Getter
public class ApplicationConfiguration {
    private final String applicationName;
    private final float appApdexThreshold;
    private final float endUserApdexThreshold;
    private final boolean enableRealUserMonitoring;

    public ApplicationConfiguration(String applicationName, float appApdexThreshold, float endUserApdexThreshold,
                                    boolean enableRealUserMonitoring) {
        this.applicationName = applicationName;
        this.appApdexThreshold = appApdexThreshold;
        this.endUserApdexThreshold = endUserApdexThreshold;
        this.enableRealUserMonitoring = enableRealUserMonitoring;
    }
}
