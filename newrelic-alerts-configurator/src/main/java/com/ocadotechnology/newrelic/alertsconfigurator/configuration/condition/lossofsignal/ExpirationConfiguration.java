package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.lossofsignal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.concurrent.TimeUnit;

/**
 * Expiration configuration to customise the loss of signal detection
 * Configuration parameters:
 * <ul>
 *     <li>{@link #expirationDuration}</li>
 *     <li>{@link #openViolationOnExpiration}</li>
 *     <li>{@link #closeViolationsOnExpiration}</li>
 * </ul>
 */
@Builder
@Value
public class ExpirationConfiguration {
    /**
     * The wait duration (in seconds) since the last data point is received before considering the signal is lost
     */
    long expirationDuration;

    /**
     * If {@code true} opens a loss of signal violation when there was no signal within the {@link #expirationDuration} time
     * Requires a value to be set in {@link #expirationDuration}
     */
    boolean openViolationOnExpiration;

    /**
     * If {@code true} closes all the open violations when there was no signal within the {@link #expirationDuration}
     * Requires a value to be set in {@link #expirationDuration}
     */
    boolean closeViolationsOnExpiration;

    public static class ExpirationConfigurationBuilder {
        public ExpirationConfigurationBuilder expirationDuration(int duration, TimeUnit unit) {
            this.expirationDuration = unit.toSeconds(duration);
            return this;
        }

        public ExpirationConfigurationBuilder expirationDuration(ExpirationDuration duration){
            this.expirationDuration = duration.getTimeInSeconds();
            return this;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum ExpirationDuration {
        SECOND(1),
        SECONDS_2(2),
        SECONDS_3(3),
        SECONDS_4(4),
        SECONDS_5(5),
        SECONDS_10(10),
        SECONDS_15(15),
        SECONDS_30(30),
        MINUTE(60),
        MINUTES_2(120),
        MINUTES_3(180),
        MINUTES_4(240),
        MINUTES_5(300),
        MINUTES_10(600),
        MINUTES_15(900),
        MINUTES_30(1800);

        private final int timeInSeconds;
    }
}
