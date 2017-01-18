package com.ocado.panda.newrelic.sync.configuration.condition;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class UserDefinedConfiguration {
    @NonNull
    private String metric;
    @NonNull
    private ValueFunction valueFunction;

    public enum ValueFunction {
        AVERAGE, MIN, MAX, TOTAL, SAMPLE_SIZE;

        public String getAsString() {
            return this.name().toLowerCase();
        }
    }
}
