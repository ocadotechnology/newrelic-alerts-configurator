package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal;

import com.ocadotechnology.newrelic.apiclient.model.conditions.Signal;

public final class SignalUtils {

    private SignalUtils() {
    }

    public static Signal createSignal(NrqlSignalConfiguration nrqlSignalConfiguration) {
        return Signal.builder()
            .aggregationMethod(nrqlSignalConfiguration.getAggregationMethod().getValue())
            .aggregationDelay(intToStringOrNull(nrqlSignalConfiguration.getAggregationDelay()))
            .aggregationTimer(intToStringOrNull(nrqlSignalConfiguration.getAggregationTimer()))
            .aggregationWindow(intToStringOrNull(nrqlSignalConfiguration.getAggregationWindow()))
            .fillOption(nrqlSignalConfiguration.getSignalFillOption().getValue())
            .fillValue(nrqlSignalConfiguration.getSignalFillValue())
            .build();
    }

    public static String intToStringOrNull(Integer integer) {
        return integer != null ? String.valueOf(integer) : null;
    }
}
