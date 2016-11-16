package com.ocado.pandateam.newrelic.sync.configuration.condition;

import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.DurationTerm;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.OperatorTerm;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.PriorityTerm;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.TimeFunctionTerm;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class ApmAppCondition implements Condition {
    @NonNull
    private String conditionName;
    private boolean enabled;
    @NonNull
    private Metric metric;
    private String runBookUrl;
    @NonNull
    private DurationTerm durationTerm;
    @NonNull
    private OperatorTerm operatorTerm;
    @NonNull
    private PriorityTerm priorityTerm;
    @NonNull
    private int thresholdTerm;
    @NonNull
    private TimeFunctionTerm timeFunctionTerm;

    @Override
    public String getMetric() {
        return metric.name().toLowerCase();
    }

    @Override
    public String getDurationTerm() {
        return durationTerm.name().toLowerCase();
    }

    @Override
    public String getOperatorTerm() {
        return operatorTerm.name().toLowerCase();
    }

    @Override
    public String getPriorityTerm() {
        return priorityTerm.name().toLowerCase();
    }

    @Override
    public String getTimeFunctionTerm() {
        return timeFunctionTerm.name().toLowerCase();
    }

    public enum Metric {
        APDEX, ERROR_PERCENTAGE, RESPONSE_TIME_WEB, RESPONSE_TIME_BACKGROUND, THROUGHPUT_WEB, THROUGHPUT_BACKGROUND, USER_DEFINED
    }
}
