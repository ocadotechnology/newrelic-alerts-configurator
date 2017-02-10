package com.ocado.newrelic.alertconfiguploader.configuration.condition.terms;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
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
}
