package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql;

import lombok.Getter;

@Getter
public enum SinceValue {
    SINCE_1_MINUTE("1"),
    SINCE_2_MINUTES("2"),
    SINCE_3_MINUTES("3"),
    SINCE_4_MINUTES("4"),
    SINCE_5_MINUTES("5"),
    SINCE_10_MINUTES("10"),
    SINCE_15_MINUTES("15"),
    SINCE_30_MINUTES("30"),
    SINCE_60_MINUTES("60");

    String since;

    SinceValue(String since) {
        this.since = since;
    }
}
