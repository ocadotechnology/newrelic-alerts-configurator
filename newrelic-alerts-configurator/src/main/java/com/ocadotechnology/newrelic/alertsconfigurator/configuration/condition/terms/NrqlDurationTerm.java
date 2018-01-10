package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NrqlDurationTerm {
    DURATION_1(1),
    DURATION_2(2),
    DURATION_3(3),
    DURATION_4(4),
    DURATION_5(5),
    DURATION_10(10),
    DURATION_15(15),
    DURATION_30(30),
    DURATION_60(60),
    DURATION_120(120);

    final int duration;
}
