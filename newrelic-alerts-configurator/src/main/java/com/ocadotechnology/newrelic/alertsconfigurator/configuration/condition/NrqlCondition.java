package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal.NrqlSignalConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.NrqlTermsConfiguration;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

/**
 * NRQL condition.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #conditionName}</li>
 *     <li>{@link #enabled} (optional)</li>
 *     <li>{@link #runBookUrl} (optional)</li>
 *     <li>{@link #terms}</li>
 *     <li>{@link #valueFunction}</li>
 *     <li>{@link #query}</li>
 *     <li>{@link #signal}</li>
 * </ul>
 */
@Getter
@Builder
public class NrqlCondition {

    public static final int DEFAULT_VIOLATION_TIME_LIMIT_SECONDS = 259_200;

    /**
     * Name of your NRQL condition
     */
    @NonNull
    private String conditionName;
    /**
     * If your NRQL condition is enabled. Default is true
     */
    @Builder.Default
    private boolean enabled = true;

    /**
     * Used to automatically close instance-based incidents after the number of seconds specified.
     * <br>
     * Default is {@link #DEFAULT_VIOLATION_TIME_LIMIT_SECONDS}. Maximum is 30 days.
     * <br>
     * Used for:
     * <li>Location conditions</li>
     * <li>NRQL conditions</li>

     * @see <a href="https://docs.newrelic.com/docs/alerts-applied-intelligence/new-relic-alerts/advanced-alerts/rest-api-alerts/alerts-conditions-api-field-names/#violation_time_limit_seconds">Alerts conditions API field names - violation_time_limit_seconds</a>
     */
    @Builder.Default
    private Integer violationTimeLimitSeconds = DEFAULT_VIOLATION_TIME_LIMIT_SECONDS;
    /**
     * The runbook URL to display in notifications
     */
    private String runBookUrl;
    /**
     * Collection of terms used for alerts condition
     */
    @NonNull
    @Singular
    private Collection<NrqlTermsConfiguration> terms;
    /**
     * Value function
     */
    @NonNull
    private ValueFunction valueFunction;
    /**
     * This is NRQL query that will be executed by the condition.
     */
    @NonNull
    private String query;

    /**
     * NRQL Signal configuration.
     */
    private NrqlSignalConfiguration signal;

    @Getter
    @AllArgsConstructor
    public enum ValueFunction {
        SINGLE_VALUE("single_value"),
        SUM("sum");

        private final String valueString;
    }
}
