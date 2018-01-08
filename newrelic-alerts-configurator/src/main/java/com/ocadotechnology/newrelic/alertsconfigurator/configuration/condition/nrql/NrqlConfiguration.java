package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
/**
 * NRQL configuration.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #query}</li>
 *     <li>{@link #sinceValue}</li>
 * </ul>
 */
@Builder
@Getter
public class NrqlConfiguration {
    /**
     * New Relic query
     */
    @NonNull
    private String query;
    /**
     * Evaluating data offset
     */
    @NonNull
    private SinceValue sinceValue;
}