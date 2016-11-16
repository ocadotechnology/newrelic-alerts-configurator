package com.ocado.pandateam.newrelic.sync.configuration;

import com.ocado.pandateam.newrelic.sync.configuration.condition.Condition;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
@Builder
public class ConditionConfiguration {
    @NonNull
    private String applicationName;
    @NonNull
    private String policyName;
    @NonNull
    private List<Condition> conditions;
}
