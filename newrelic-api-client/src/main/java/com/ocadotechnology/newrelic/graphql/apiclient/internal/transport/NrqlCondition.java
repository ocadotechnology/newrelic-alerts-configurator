package com.ocadotechnology.newrelic.graphql.apiclient.internal.transport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
public class NrqlCondition {
    Boolean enabled;
    String id;
    String name;
    String type;
    String runbookUrl;
    Nrql nrql;
    Expiration expiration;
    Signal signal;
    List<Term> terms;
    String valueFunction;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Value
    public static class Nrql {
        String query;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Value
    public static class Expiration {
        String expirationDuration;
        boolean openViolationOnExpiration;
        boolean closeViolationsOnExpiration;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Value
    public static class Signal {
        String aggregationMethod;
        String aggregationDelay;
        String aggregationTimer;
        String aggregationWindow;
        String fillOption;
        String fillValue;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Value
    public static class Term {
        String operator;
        String priority;
        String threshold;
        String thresholdDuration;
        String thresholdOccurrences;
    }
}
