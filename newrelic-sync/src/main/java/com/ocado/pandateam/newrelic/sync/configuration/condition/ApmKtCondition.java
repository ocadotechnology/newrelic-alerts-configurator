package com.ocado.pandateam.newrelic.sync.configuration.condition;

import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.TermsConfiguration;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;

@Getter
@Builder
public class ApmKtCondition implements Condition {
    private final ConditionType type = ConditionType.APM_KT;
    @NonNull
    private String conditionName;
    private boolean enabled;
    @NonNull
    private Collection<String> entities;
    @NonNull
    private Metric metric;
    private String runBookUrl;
    @NonNull
    private Collection<TermsConfiguration> terms;

    @Override
    public String getTypeString() {
        return type.getTypeString();
    }

    @Override
    public String getMetric() {
        return metric.name().toLowerCase();
    }

    @Override
    public String getConditionScope() {
        return null;
    }

    public enum Metric {
        APDEX, ERROR_PERCENTAGE, ERROR_COUNT, RESPONSE_TIME, THROUGHPUT
    }
}
