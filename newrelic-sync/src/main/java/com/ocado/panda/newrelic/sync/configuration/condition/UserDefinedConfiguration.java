package com.ocado.panda.newrelic.sync.configuration.condition;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * User defined metric configuration.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #metric}</li>
 *     <li>{@link #valueFunction}</li>
 * </ul>
 */
@Builder
@Getter
public class UserDefinedConfiguration {
    /**
     * This is the name of a user defined custom metric to be used to determine if an event should be triggered.
     */
    @NonNull
    private String metric;
    /**
     * This is the numeric value obtained from the custom metric specified by {@link #metric}
     */
    @NonNull
    private ValueFunction valueFunction;

    public enum ValueFunction {
        AVERAGE, MIN, MAX, TOTAL, SAMPLE_SIZE;

        public String getAsString() {
            return this.name().toLowerCase();
        }
    }
}
