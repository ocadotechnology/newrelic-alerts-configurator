package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * NRQL Signal configuration.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #aggregationWindow}</li>
 *     <li>{@link #evaluationWindows}</li>
 *     <li>{@link #signalFillOption}</li>
 *     <li>{@link #signalLostConfiguration}</li>
 * </ul>
 */
@Builder
@Getter
public class NrqlSignalConfiguration {

    /**
     * Time (in seconds) for how long NewRelic collects data before running the NRQL query.
     */
    @NonNull
    private Integer aggregationWindow;

    /**
     * Number of windows to evaluate data.
     */
    @NonNull
    private Integer evaluationWindows;

    /**
     * Configuration of filling data gaps/signal lost.
     */
    @NonNull
    private SignalFillOption signalFillOption;

    /**
     * Configuration of signal lost behaviour.
     */
    @NonNull
    private SignalLostConfiguration signalLostConfiguration;

}
