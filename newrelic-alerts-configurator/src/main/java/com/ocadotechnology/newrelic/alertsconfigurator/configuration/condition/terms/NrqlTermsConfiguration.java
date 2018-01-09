package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Term configuration.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #durationTerm}</li>
 *     <li>{@link #operatorTerm}</li>
 *     <li>{@link #priorityTerm}</li>
 *     <li>{@link #thresholdTerm}</li>
 *     <li>{@link #timeFunctionTerm}</li>
 * </ul>
 */
@Builder
@Getter
public class NrqlTermsConfiguration {
    /**
     * Time (in minutes) for the condition to persist before triggering an event
     */
    @NonNull
    private NrqlDurationTerm durationTerm;
    /**
     * Determines what comparison will be used between the monitored value and the threshold term value to trigger an event
     */
    @NonNull
    private OperatorTerm operatorTerm;
    /**
     * Severity level for given term in condition
     */
    @NonNull
    private PriorityTerm priorityTerm;
    /**
     * Threshold that the monitored value must be compared to using the operator term for an event to be triggered
     */
    @NonNull
    private Float thresholdTerm;
    /**
     * Time function in which threshold term have to be reached in duration term to trigger an event
     */
    @NonNull
    private TimeFunctionTerm timeFunctionTerm;
}
