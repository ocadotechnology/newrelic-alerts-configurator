package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal.NrqlSignalConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.NrqlTermsConfiguration;
import java.util.Collection;
import java.util.Optional;
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
