package com.ocado.pandateam.newrelic.sync.configuration.condition.terms;

import lombok.Builder;
import lombok.NonNull;

@Builder
public class TermsConfiguration {
    @NonNull
    private DurationTerm durationTerm;
    @NonNull
    private OperatorTerm operatorTerm;
    @NonNull
    private PriorityTerm priorityTerm;
    @NonNull
    private Float thresholdTerm;
    @NonNull
    private TimeFunctionTerm timeFunctionTerm;

    public String getDurationTerm() {
        return String.valueOf(durationTerm.duration);
    }

    public String getOperatorTerm() {
        return operatorTerm.name().toLowerCase();
    }

    public String getPriorityTerm() {
        return priorityTerm.name().toLowerCase();
    }

    public String getThresholdTerm() {
        return String.valueOf(thresholdTerm);
    }

    public String getTimeFunctionTerm() {
        return timeFunctionTerm.name().toLowerCase();
    }
}
