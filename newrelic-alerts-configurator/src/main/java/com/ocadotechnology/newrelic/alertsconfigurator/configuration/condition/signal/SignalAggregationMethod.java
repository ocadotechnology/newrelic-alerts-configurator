package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SignalAggregationMethod {
    EVENT_FLOW("EVENT_FLOW"), EVENT_TIMER("EVENT_TIMER"), CADENCE("CADENCE");

    private final String value;
}
