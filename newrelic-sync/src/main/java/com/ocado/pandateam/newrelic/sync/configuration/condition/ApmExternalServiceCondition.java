package com.ocado.pandateam.newrelic.sync.configuration.condition;

import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.TermsConfiguration;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Collection;

@Getter
@Builder
public class ApmExternalServiceCondition implements ExternalServiceCondition {
    private final ExternalServiceConditionType type = ExternalServiceConditionType.APM;
    @NonNull
    private String conditionName;
    private boolean enabled;
    @NonNull
    @Singular
    private Collection<String> entities;
    @NonNull
    private String externalServiceUrl;
    @NonNull
    private ApmAppCondition.Metric metric;
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

    public enum Metric {
        RESPONSE_TIME_AVERAGE, RESPONSE_TIME_MINIMUM, RESPONSE_TIME_MAXIMUM, THROUGHPUT
    }
}
