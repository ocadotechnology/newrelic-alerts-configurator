package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * NRQL Signal lost configuration.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #signalIsLostAfter}</li>
 *     <li>{@link #openNewViolationOnSignalLost}</li>
 *     <li>{@link #closeCurrentViolationsOnSignalLost}</li>
 * </ul>
 */
@Builder
@Getter
public class SignalLostConfiguration {

    /**
     * Time (in seconds) for the signal treated as lost.
     */
    @NonNull
    private Integer signalIsLostAfter;

    /**
     * When true, this opens a loss of signal violation when no signal within {@link #signalIsLostAfter} time.
     */
    private boolean openNewViolationOnSignalLost;

    /**
     * When true, this closes all currently open violations when no signal is heard within {@link #signalIsLostAfter} time.
     */
    private boolean closeCurrentViolationsOnSignalLost;

}
