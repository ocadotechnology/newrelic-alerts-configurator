package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.lossofsignal;

import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.Expiration;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.Signal;

public class LossOfSignalUtils {
    private LossOfSignalUtils() {
    }

    public static Expiration createExpiration(ExpirationConfiguration expiration) {
        return Expiration.builder()
                .expirationDuration(String.valueOf(expiration.getExpirationDuration()))
                .closeViolationsOnExpiration(expiration.isCloseViolationsOnExpiration())
                .openViolationOnExpiration(expiration.isOpenViolationOnExpiration())
                .build();
    }

    public static Signal createSignal(SignalConfiguration signal) {
        return Signal.builder()
                .evaluationOffset(String.valueOf(signal.getEvaluationOffset()))
                .aggregationWindow(String.valueOf(signal.getAggregationWindow()))
                .fillValue(signal.getFillValue())
                .fillOption(signal.getFillOption().getValue())
                .build();
    }
}
