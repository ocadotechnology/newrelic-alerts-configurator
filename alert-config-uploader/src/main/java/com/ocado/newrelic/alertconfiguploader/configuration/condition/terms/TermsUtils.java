package com.ocado.newrelic.alertconfiguploader.configuration.condition.terms;

import com.ocado.newrelic.api.model.conditions.Terms;

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
            .duration(String.valueOf(termsConfiguration.getDurationTerm().getDuration()))
            .operator(termsConfiguration.getOperatorTerm().name().toLowerCase())
            .priority(termsConfiguration.getPriorityTerm().name().toLowerCase())
            .threshold(String.valueOf(termsConfiguration.getThresholdTerm()))
            .timeFunction(termsConfiguration.getTimeFunctionTerm().name().toLowerCase())
            .build();
    }
}
