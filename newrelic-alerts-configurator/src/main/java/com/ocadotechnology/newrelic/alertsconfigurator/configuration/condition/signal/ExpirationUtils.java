package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal;

import com.ocadotechnology.newrelic.apiclient.model.conditions.Expiration;

public final class ExpirationUtils {

    private ExpirationUtils() {
    }

    public static Expiration createExpiration(SignalLostConfiguration signalLostConfiguration) {
        return Expiration.builder()
            .expirationDuration(String.valueOf(signalLostConfiguration.getSignalIsLostAfter()))
            .closeViolationsOnExpiration(signalLostConfiguration.isCloseCurrentViolationsOnSignalLost())
            .openViolationOnExpiration(signalLostConfiguration.isOpenNewViolationOnSignalLost())
            .build();
    }
}
