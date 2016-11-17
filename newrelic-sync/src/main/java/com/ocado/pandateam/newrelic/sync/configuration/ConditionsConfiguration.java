package com.ocado.pandateam.newrelic.sync.configuration;

import com.ocado.pandateam.newrelic.sync.configuration.condition.Condition;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;

@Getter
@Builder
public class ConditionsConfiguration {
    @NonNull
    private String policyName;
    @NonNull
    private Collection<Condition> conditions;
}
