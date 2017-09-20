package com.ocadotechnology.newrelic.alertsconfigurator.configuration;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.Channel;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.Condition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ExternalServiceCondition;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Collection;
import java.util.Collections;

/**
 * Policy configuration.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #policyName}</li>
 *     <li>{@link #incidentPreference} (optional)</li>
 *     <li>{@link #channels} (optional)</li>
 *     <li>{@link #conditions} (optional)</li>
 *     <li>{@link #externalServiceConditions} (optional)</li>
 * </ul>
 */
@Getter
@Builder
public class PolicyConfiguration {
    /**
     * Your policy name
     */
    @NonNull
    private String policyName;
    /**
     * Rollup strategy options for your alerts policy
     */
    @NonNull
    private IncidentPreference incidentPreference;
    /**
     * Collection of {@link Channel} configurations
     */
    @Singular
    private Collection<Channel> channels = Collections.emptyList();
    /**
     * Collection of {@link Condition} configurations
     */
    @Singular
    private Collection<Condition> conditions = Collections.emptyList();
    /**
     * Collection of {@link ExternalServiceCondition} configurations
     */
    @Singular
    private Collection<ExternalServiceCondition> externalServiceConditions = Collections.emptyList();

    public enum IncidentPreference {
        PER_POLICY, PER_CONDITION, PER_CONDITION_AND_TARGET
    }
}
