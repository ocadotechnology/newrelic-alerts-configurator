package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsConfiguration;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Collection;

/**
 * APM key transaction configuration.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #conditionName}</li>
 *     <li>{@link #enabled}</li>
 *     <li>{@link #entities}</li>
 *     <li>{@link #metric}</li>
 *     <li>{@link #runBookUrl}</li>
 *     <li>{@link #terms}</li>
 * </ul>
 */
@Getter
@Builder
public class ApmKeyTransactionCondition implements Condition {
    private final ConditionType type = ConditionType.APM_KEY_TRANSACTION;
    /**
     * Name of your APM key transaction metric condition
     */
    @NonNull
    private String conditionName;
    /**
     * If your APM key transaction metric condition is enabled. Default is false
     */
    private boolean enabled;
    /**
     * Collection of key transaction names for which this condition is applied.
     * If key transaction with given name does not exist exception will be thrown
     */
    @NonNull
    @Singular
    private Collection<String> entities;
    /**
     * Metric used in given condition
     */
    @NonNull
    private Metric metric;
    /**
     * The runbook URL to display in notifications
     */
    private String runBookUrl;
    /**
     * Collection of terms used for alerts condition
     */
    @NonNull
    @Singular
    private Collection<TermsConfiguration> terms;

    @Override
    public String getMetricAsString() {
        return metric.name().toLowerCase();
    }

    public enum Metric {
        APDEX, ERROR_PERCENTAGE, ERROR_COUNT, RESPONSE_TIME, THROUGHPUT
    }
}
