package com.ocado.pandateam.newrelic.sync.configuration;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PolicyConfiguration {
    private String applicationName;
    private String policyName;
}
