package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Synthetics condition.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #conditionName}</li>
 *     <li>{@link #monitorName}</li>
 *     <li>{@link #enabled} (optional)</li>
 *     <li>{@link #runBookUrl} (optional)</li>
 * </ul>
 */
@Getter
@Builder
public class SyntheticsCondition {
    /**
     * Name of your Synthetics condition
     */
    @NonNull
    private String conditionName;
    /**
     * Name of Synthetics monitor
     */
    @NonNull
    private String monitorName;
    /**
     * If your Synthetics condition is enabled. Default is true
     */
    @Builder.Default
    private boolean enabled = true;
    /**
     * The runbook URL to display in notifications
     */
    private String runBookUrl;

}
