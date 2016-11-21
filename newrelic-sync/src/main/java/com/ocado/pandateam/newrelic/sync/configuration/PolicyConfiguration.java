package com.ocado.pandateam.newrelic.sync.configuration;

import com.ocado.pandateam.newrelic.sync.configuration.channel.Channel;
import com.ocado.pandateam.newrelic.sync.configuration.condition.Condition;
import com.ocado.pandateam.newrelic.sync.configuration.condition.ExternalServiceCondition;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Collection;
import java.util.Collections;

@Getter
@Builder
public class PolicyConfiguration {
    @NonNull
    private String policyName;
    private IncidentPreference incidentPreference;
    @Singular
    private Collection<Channel> channels = Collections.emptyList();
    @Singular
    private Collection<Condition> conditions = Collections.emptyList();
    @Singular
    private Collection<ExternalServiceCondition> externalServiceConditions = Collections.emptyList();

    public String getIncidentPreference() {
        return incidentPreference == null ? null : incidentPreference.name();
    }

    public enum IncidentPreference {
        PER_POLICY, PER_CONDITION, PER_CONDITION_AND_TARGET
    }
}
