package com.ocadotechnology.newrelic.alertconfiguploader.configuration;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Application configuration.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #applicationName}</li>
 *     <li>{@link #appApdexThreshold} (optional)</li>
 *     <li>{@link #endUserApdexThreshold} (optional)</li>
 *     <li>{@link #enableRealUserMonitoring} (optional)</li>
 * </ul>
 */
@Getter
@Builder
public class ApplicationConfiguration {
    /**
     * Your application name
     */
    @NonNull
    private String applicationName;
    /**
     * Apdex threshold for application
     */
    private float appApdexThreshold;
    /**
     * Apdex threshold for end user
     */
    private float endUserApdexThreshold;
    /**
     * If real user monitoring is enabled. Default value is false
     */
    private boolean enableRealUserMonitoring;
}
