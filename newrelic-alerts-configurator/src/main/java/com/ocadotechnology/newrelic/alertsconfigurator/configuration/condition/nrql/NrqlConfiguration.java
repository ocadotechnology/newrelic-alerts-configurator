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
     * This is NRQL query that will be executed by the condition.
     */
    @NonNull
    private String query;
    /**
     * This is the timeframe in which to evaluate the {@link #query}
     */
    @NonNull
    private SinceValue sinceValue;

    public enum SinceValue {
        SINCE_1(1),
        SINCE_2(2),
        SINCE_3(3),
        SINCE_4(4),
        SINCE_5(5);

        int since;

        SinceValue(int since) {
            this.since = since;
        }
    }
}
