package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.violationclosetimer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum ViolationCloseTimer {
    DURATION_1("1"),
    DURATION_2("2"),
    DURATION_4("4"),
    DURATION_8("8"),
    DURATION_12("12"),
    DURATION_24("24");

    String duration;
}
