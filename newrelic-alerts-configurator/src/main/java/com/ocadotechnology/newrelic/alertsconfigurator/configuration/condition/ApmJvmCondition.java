package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.violationclosetimer.ViolationCloseTimer;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Collection;

/**
 * APM JVM configuration.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #conditionName}</li>
 *     <li>{@link #enabled}</li>
 *     <li>{@link #applications}</li>
 *     <li>{@link #metric}</li>
 *     <li>{@link #runBookUrl}</li>
 *     <li>{@link #terms}</li>
 *     <li>{@link #violationCloseTimer}</li>
 * </ul>
 */
@Getter
@Builder
public class ApmJvmCondition implements Condition {
    private final ConditionType type = ConditionType.APM_JVM;
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
    private Collection<String> applications;
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
    /**
     * Duration (in hours) after which violations will automatically close.
     */
    @NonNull
    private ViolationCloseTimer violationCloseTimer;

    @Override
    public Collection<String> getEntities() {
        return getApplications();
    }

    @Override
    public String getMetricAsString() {
        return metric.name().toLowerCase();
    }

    @Override
    public String getViolationCloseTimerAsString() {
        return violationCloseTimer == null ? null : violationCloseTimer.getDuration();
    }

    public enum Metric {
        DEADLOCKED_THREADS, HEAP_MEMORY_USAGE, CPU_UTILIZATION_TIME
    }

}
