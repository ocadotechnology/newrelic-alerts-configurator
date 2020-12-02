package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SignalFillOption {
    NONE("none"), LAST_KNOWN_VALUE("last_value");

    private final String value;

}
