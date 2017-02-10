package com.ocado.newrelic.alertconfiguploader.configuration.condition.terms;

import lombok.Getter;

@Getter
public enum DurationTerm {
    DURATION_5(5),
    DURATION_10(10),
    DURATION_15(15),
    DURATION_30(30),
    DURATION_60(60),
    DURATION_120(120);

    int duration;

    DurationTerm(int duration) {
        this.duration = duration;
    }
}
