package com.ocado.pandateam.newrelic.sync.configuration.condition.terms;

import com.ocado.pandateam.newrelic.api.model.conditions.Terms;

import java.util.Collection;
import java.util.stream.Collectors;

public final class TermsUtils {

    private TermsUtils() {
    }

    public static Collection<Terms> createTerms(Collection<TermsConfiguration> termsConfigurations) {
        return termsConfigurations.stream().map(TermsUtils::mapTerms).collect(Collectors.toList());
    }

    private static Terms mapTerms(TermsConfiguration termsConfiguration) {
        return Terms.builder()
            .duration(termsConfiguration.getDurationTerm())
            .operator(termsConfiguration.getOperatorTerm())
            .priority(termsConfiguration.getPriorityTerm())
            .threshold(termsConfiguration.getThresholdTerm())
            .timeFunction(termsConfiguration.getTimeFunctionTerm())
            .build();
    }
}
