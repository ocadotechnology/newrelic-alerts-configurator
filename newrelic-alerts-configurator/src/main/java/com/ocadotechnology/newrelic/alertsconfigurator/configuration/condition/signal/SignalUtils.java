package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal;

import com.ocadotechnology.newrelic.apiclient.model.conditions.Signal;

public final class SignalUtils {

    private SignalUtils() {
    }

    public static Signal createSignal(NrqlSignalConfiguration nrqlSignalConfiguration) {
        return Signal.builder()
            .aggregationWindow(String.valueOf(nrqlSignalConfiguration.getAggregationWindow()))
            .evaluationOffset(String.valueOf(nrqlSignalConfiguration.getEvaluationWindows()))
            .fillOption(nrqlSignalConfiguration.getSignalFillOption().getValue())
            .fillValue(null)
            .build();
    }
}
