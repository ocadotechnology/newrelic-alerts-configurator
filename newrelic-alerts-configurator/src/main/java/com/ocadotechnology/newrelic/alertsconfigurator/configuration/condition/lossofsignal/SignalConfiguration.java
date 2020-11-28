package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.lossofsignal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.concurrent.TimeUnit;

/**
 * Signal configuration to customise the gap filling
 * Configuration parameters:
 * <ul>
 *     <li>{@link #fillOption}</li>
 *     <li>{@link #fillValue}</li>
 *     <li>{@link #aggregationWindow}</li>
 *     <li>{@link #evaluationOffset}</li>
 * </ul>
 */
@Builder
@Getter
public class SignalConfiguration {
    /**
     * Fill option to avoid false alerts by filling the gaps with synthetic data from {@link #fillValue}
     */
    @NonNull
    @Builder.Default
    private FillOption fillOption = FillOption.NONE;

    /**
     * The custom static value used when {@link FillOption#STATIC} is set
     */
    @NonNull
    private String fillValue;

    /**
     * The window of time (in minutes) defining how streamed data points will be gathered together before executing NRQL query
     */
    @NonNull
    private long aggregationWindow;

    /**
     * The evaluation offset (in minutes) defining how long we should wait for late data before evaluating each aggregation windows
     */
    @NonNull
    private long evaluationOffset;

    public static class SignalConfigurationBuilder{
        public SignalConfigurationBuilder aggregationWindow(AggregationWindow window){
            this.aggregationWindow = window.getTimeInMinutes();
            return this;
        }

        public SignalConfigurationBuilder aggregationWindow(long duration, TimeUnit unit){
            this.aggregationWindow = unit.toMinutes(duration);
            return this;
        }

        public SignalConfigurationBuilder evaluationOffset(EvaluationOffset offset){
            this.evaluationOffset = offset.timeInMinutes;
            return this;
        }

        public SignalConfigurationBuilder evaluationOffset(long duration, TimeUnit unit){
            this.evaluationOffset = unit.toMinutes(duration);
            return this;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum FillOption {
        NONE("none"),
        LAST_VALUE("last_value"),
        STATIC("static");

        private final String value;
    }


    @Getter
    @AllArgsConstructor
    public enum AggregationWindow {
        WINDOW_1(1),
        WINDOW_2(2),
        WINDOW_3(3),
        WINDOW_4(4),
        WINDOW_5(5),
        WINDOW_6(6),
        WINDOW_7(7),
        WINDOW_8(8),
        WINDOW_9(9),
        WINDOW_10(10),
        WINDOW_11(11),
        WINDOW_12(12),
        WINDOW_13(13),
        WINDOW_14(14),
        WINDOW_15(15);

        private final int timeInMinutes;
    }

    @Getter
    @AllArgsConstructor
    public enum EvaluationOffset {
        OFFSET_1(1),
        OFFSET_2(2),
        OFFSET_3(3),
        OFFSET_4(4),
        OFFSET_5(5),
        OFFSET_10(10),
        OFFSET_15(15),
        OFFSET_30(30),
        OFFSET_60(60),
        OFFSET_120(120);

        private final int timeInMinutes;
    }

}
