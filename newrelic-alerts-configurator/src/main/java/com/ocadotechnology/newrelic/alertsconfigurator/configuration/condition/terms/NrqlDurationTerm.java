package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms;

import lombok.Getter;

@Getter
public final class NrqlDurationTerm {
//    defaults as used in original, enum-based implementation
    public static final NrqlDurationTerm DURATION_1 = new NrqlDurationTerm(1);
    public static final NrqlDurationTerm DURATION_2 = new NrqlDurationTerm(2);
    public static final NrqlDurationTerm DURATION_3 = new NrqlDurationTerm(3);
    public static final NrqlDurationTerm DURATION_4 = new NrqlDurationTerm(4);
    public static final NrqlDurationTerm DURATION_5 = new NrqlDurationTerm(5);
    public static final NrqlDurationTerm DURATION_10 = new NrqlDurationTerm(10);
    public static final NrqlDurationTerm DURATION_15 = new NrqlDurationTerm(15);
    public static final NrqlDurationTerm DURATION_30 = new NrqlDurationTerm(30);
    public static final NrqlDurationTerm DURATION_60 = new NrqlDurationTerm(60);
    public static final NrqlDurationTerm DURATION_120 = new NrqlDurationTerm(120);

    private final int duration;

    public NrqlDurationTerm(int duration) {
        assertMoreThanZero(duration);
        this.duration = duration;
    }

    private static void assertMoreThanZero(int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration Term for alert should not be 0 nor negative");
        }
    }
}
