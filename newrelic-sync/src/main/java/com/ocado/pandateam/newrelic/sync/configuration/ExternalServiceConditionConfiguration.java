package com.ocado.pandateam.newrelic.sync.configuration;

import com.ocado.pandateam.newrelic.sync.configuration.condition.ExternalServiceCondition;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;

@Getter
@Builder
public class ExternalServiceConditionConfiguration {
    @NonNull
    private String policyName;
    @NonNull
    private Collection<ExternalServiceCondition> externalServiceConditions;
}
