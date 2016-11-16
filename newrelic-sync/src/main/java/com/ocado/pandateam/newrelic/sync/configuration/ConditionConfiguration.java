package com.ocado.pandateam.newrelic.sync.configuration;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class ConditionConfiguration {
    @NonNull
    private String applicationName;
    @NonNull
    private String policyName;
}
