package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * NRQL Signal configuration.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #aggregationMethod}</li>
 *     <li>{@link #aggregationWindow}</li>
 *     <li>{@link #signalFillOption}</li>
 *     <li>{@link #signalFillValue}</li>
 *     <li>{@link #signalLostConfiguration}</li>
 * </ul>
 */
@Builder
@Getter
public class NrqlSignalConfiguration {

    /**
     * Configuration of signal aggregation method.
     * For reference see: <a href="https://docs.newrelic.com/docs/alerts-applied-intelligence/new-relic-alerts/advanced-alerts/understand-technical-concepts/streaming-alerts-key-terms-concepts/#aggregation-methods">NR Docs</a>
     */
    @NonNull
    private SignalAggregationMethod aggregationMethod;

    /**
     * Delay is how long we wait for events that belong in each aggregation window.
     * Depending on your data a longer delay may increase accuracy but delay notifications.
     * This value is used for EVENT_FLOW and CADENCE aggregation methods.
     */
    private Integer aggregationDelay;

    /**
     * The timer tells us how long to wait after each data point to make sure we've processed the whole batch.
     * This value should match data frequency, and preferably be at least equal to aggregationWindow.
     * See <a href="https://docs.newrelic.com/docs/alerts-applied-intelligence/new-relic-alerts/advanced-alerts/understand-technical-concepts/streaming-alerts-key-terms-concepts/#aggregation-methods">NR Docs</a> for more information.
     * This value is used for EVENT_TIMER aggregation method.
     */
    private Integer aggregationTimer;

    /**
     * Time (in seconds) for how long NewRelic collects data before running the NRQL query.
     */
    @NonNull
    private Integer aggregationWindow;

    /**
     * Configuration of filling data gaps/signal lost.
     */
    @NonNull
    private SignalFillOption signalFillOption;

    /**
     * Value for filling data gaps/signal lost
     */
    private String signalFillValue;

    /**
     * Configuration of signal lost behaviour.
     */
    private SignalLostConfiguration signalLostConfiguration;

}
