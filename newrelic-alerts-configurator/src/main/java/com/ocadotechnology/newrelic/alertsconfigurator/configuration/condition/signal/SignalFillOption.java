package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal;

public enum SignalFillOption {
    NONE("none"), LAST_KNOWN_VALUE("last_value");

    private String value;

    SignalFillOption(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
