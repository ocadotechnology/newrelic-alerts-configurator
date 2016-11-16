package com.ocado.pandateam.newrelic.sync.configuration;

import lombok.Getter;

@Getter
public class PolicyConfiguration {
    private final String applicationName;
    private final String policyName;

    public PolicyConfiguration(String applicationName, String policyName) {
        this.applicationName = applicationName;
        this.policyName = policyName;
    }
}
