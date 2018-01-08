package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ValueFunction {
    SINGLE_VALUE("single_value"),
    SUM("sum");

    private final String valueString;
}
