package com.ocadotechnology.newrelic.alertconfiguploader.configuration.condition;

import com.ocadotechnology.newrelic.alertconfiguploader.configuration.condition.terms.TermsConfiguration;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Collection;

/**
 * APM application configuration.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #conditionName}</li>
 *     <li>{@link #enabled}</li>
 *     <li>{@link #entities}</li>
 *     <li>{@link #metric}</li>
 *     <li>{@link #conditionScope}</li>
 *     <li>{@link #runBookUrl}</li>
 *     <li>{@link #terms}</li>
 *     <li>{@link #userDefinedConfiguration}</li>
 * </ul>
 */
@Getter
@Builder
public class ApmAppCondition implements Condition {
    private final ConditionType type = ConditionType.APM_APP;
    /**
     * Name of your APM application metric condition
     */
    @NonNull
    private String conditionName;
    /**
     * If your APM application metric condition is enabled. Default is false
     */
    private boolean enabled;
    /**
     * Collection of application names for which this condition is applied.
     * If application with given name does not exist exception will be thrown
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
     * Condition scope used in given condition
     */
    @NonNull
    private ConditionScope conditionScope;
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
    /**
     * Configuration for user defined metric. Should be provided when {@link #metric} is set to USER_DEFINED
     */
    private UserDefinedConfiguration userDefinedConfiguration;

    @Override
    public String getMetricAsString() {
        return metric.name().toLowerCase();
    }

    @Override
    public String getConditionScopeAsString() {
        return conditionScope.name().toLowerCase();
    }

    @Override
    public UserDefinedConfiguration getUserDefinedMetric() {
        return userDefinedConfiguration;
    }

    public enum Metric {
        APDEX, ERROR_PERCENTAGE, RESPONSE_TIME_WEB, RESPONSE_TIME_BACKGROUND, THROUGHPUT_WEB, THROUGHPUT_BACKGROUND, USER_DEFINED
    }

    public enum ConditionScope {
        APPLICATION, INSTANCE
    }

}
