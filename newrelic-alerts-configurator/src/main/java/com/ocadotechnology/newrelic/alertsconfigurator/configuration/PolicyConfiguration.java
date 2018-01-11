package com.ocadotechnology.newrelic.alertsconfigurator.configuration;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.Channel;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.Condition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ExternalServiceCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.NrqlCondition;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;

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
    private Collection<Channel> channels;
    /**
     * Collection of {@link Condition} configurations
     */
    @Singular
    private Collection<Condition> conditions;
    /**
     * Collection of {@link ExternalServiceCondition} configurations
     */
    @Singular
    private Collection<ExternalServiceCondition> externalServiceConditions;
    /**
     * Collection of {@link NrqlCondition} configurations
     */
    @Singular
    private Collection<NrqlCondition> nrqlConditions;

    public enum IncidentPreference {
        PER_POLICY, PER_CONDITION, PER_CONDITION_AND_TARGET
    }

    public Optional<Collection<Channel>> getChannels() {
        return Optional.ofNullable(channels);
    }

    public Optional<Collection<Condition>> getConditions() {
        return Optional.ofNullable(conditions);
    }

    public Optional<Collection<ExternalServiceCondition>> getExternalServiceConditions() {
        return Optional.ofNullable(externalServiceConditions);
    }

    public Optional<Collection<NrqlCondition>> getNrqlConditions() {
        return Optional.ofNullable(nrqlConditions);
    }

    public static class PolicyConfigurationBuilder {
        public void doNotModifyExistingConditions() {
            this.conditions = null;
        }

        public void doNotModifyExistingExternalServiceConditions() {
            this.externalServiceConditions = null;
        }

        public void doNotModifyExistingNrqlConditions() {
            this.nrqlConditions = null;
        }

        public PolicyConfiguration build() {
            return new PolicyConfiguration(
                    policyName,
                    incidentPreference,
                    normalizeNullableList(this.channels),
                    normalizeNullableList(this.conditions),
                    normalizeNullableList(this.externalServiceConditions),
                    normalizeNullableList(this.nrqlConditions));
        }

        private static <T> Collection<T> normalizeNullableList(List<T> list) {
            return list == null ? null : normalizeList(list);
        }

        private static <T> Collection<T> normalizeList(List<T> list) {
            switch (list == null ? 0 : list.size()) {
                case 0:
                    return emptyList();
                case 1:
                    return singletonList(list.get(0));
                default:
                    return unmodifiableList(new ArrayList<>(list));
            }
        }
    }
}
