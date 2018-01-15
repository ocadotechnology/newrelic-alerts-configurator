package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms;

import com.ocadotechnology.newrelic.apiclient.model.conditions.Terms;

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

    public static Collection<Terms> createNrqlTerms(Collection<NrqlTermsConfiguration> termsConfigurations) {
        return termsConfigurations.stream().map(TermsUtils::mapNrqlTerms).collect(Collectors.toList());
    }

    private static Terms mapNrqlTerms(NrqlTermsConfiguration termsConfiguration) {
        return Terms.builder()
                .duration(String.valueOf(termsConfiguration.getDurationTerm().getDuration()))
                .operator(termsConfiguration.getOperatorTerm().name().toLowerCase())
                .priority(termsConfiguration.getPriorityTerm().name().toLowerCase())
                .threshold(String.valueOf(termsConfiguration.getThresholdTerm()))
                .timeFunction(termsConfiguration.getTimeFunctionTerm().name().toLowerCase())
                .build();
    }
}
