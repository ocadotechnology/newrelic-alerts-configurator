package com.ocado.pandateam.newrelic.sync.configuration.condition;

import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.TermsConfiguration;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Collection;

@Getter
@Builder
public class ApmAppCondition implements Condition {
    private final ConditionType type = ConditionType.APM_APP;
    @NonNull
    private String conditionName;
    private boolean enabled;
    @NonNull
    @Singular
    private Collection<String> entities;
    @NonNull
    private Metric metric;
    @NonNull
    private ConditionScope conditionScope;
    private String runBookUrl;
    @NonNull
    @Singular
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
        return conditionScope.name().toLowerCase();
    }

    public enum Metric {
        APDEX, ERROR_PERCENTAGE, RESPONSE_TIME_WEB, RESPONSE_TIME_BACKGROUND, THROUGHPUT_WEB, THROUGHPUT_BACKGROUND, USER_DEFINED
    }
}
