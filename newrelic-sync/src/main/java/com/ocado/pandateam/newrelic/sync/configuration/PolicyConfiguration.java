package com.ocado.pandateam.newrelic.sync.configuration;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class PolicyConfiguration {
    @NonNull
    private String policyName;
    private IncidentPreference incidentPreference;

    public String getIncidentPreference() {
        return incidentPreference.name();
    }

    public enum IncidentPreference {
        PER_POLICY, PER_CONDITION, PER_CONDITION_AND_TARGET
    }
}
