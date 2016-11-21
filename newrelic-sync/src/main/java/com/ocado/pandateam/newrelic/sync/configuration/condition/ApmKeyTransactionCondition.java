package com.ocado.pandateam.newrelic.sync.configuration.condition;

import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.TermsConfiguration;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Collection;

@Getter
@Builder
public class ApmKeyTransactionCondition implements Condition {
    private final ConditionType type = ConditionType.APM_KEY_TRANSACTION;
    @NonNull
    private String conditionName;
    private boolean enabled;
    @NonNull
    @Singular
    private Collection<String> entities;
    @NonNull
    private Metric metric;
    private String runBookUrl;
    @NonNull
    @Singular
    private Collection<TermsConfiguration> terms;

    @Override
    public String getMetricAsString() {
        return metric.name().toLowerCase();
    }

    public enum Metric {
        APDEX, ERROR_PERCENTAGE, ERROR_COUNT, RESPONSE_TIME, THROUGHPUT
    }
}
