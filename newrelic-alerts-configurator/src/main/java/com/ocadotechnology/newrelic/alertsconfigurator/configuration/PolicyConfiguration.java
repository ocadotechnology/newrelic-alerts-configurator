package com.ocadotechnology.newrelic.alertsconfigurator.configuration;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.Channel;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.Condition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ExternalServiceCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql.NrqlCondition;
import lombok.Getter;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
public class PolicyConfiguration {
    /**
     * Your policy name
     */
    private String policyName;
    /**
     * Rollup strategy options for your alerts policy
     */
    private IncidentPreference incidentPreference;
    /**
     * Collection of {@link Channel} configurations
     */
    private Collection<Channel> channels;
    /**
     * Collection of {@link Condition} configurations
     */
    private Collection<Condition> conditions;
    /**
     * Collection of {@link ExternalServiceCondition} configurations
     */
    private Collection<ExternalServiceCondition> externalServiceConditions;
    /**
     * Collection of {@link NrqlCondition} configurations
     */
    private Collection<NrqlCondition> nrqlConditions;

    @ConstructorProperties(
            {"policyName", "incidentPreference", "channels", "conditions", "externalServiceConditions", "nrqlConditions"})
    PolicyConfiguration(String policyName,
                        IncidentPreference incidentPreference,
                        Collection<Channel> channels, Collection<Condition> conditions,
                        Collection<ExternalServiceCondition> externalServiceConditions,
                        Collection<NrqlCondition> nrqlConditions) {
        if (policyName == null) {
            throw new NullPointerException("policyName");
        }
        if (incidentPreference == null) {
            throw new NullPointerException("incidentPreference");
        }

        this.policyName = policyName;
        this.incidentPreference = incidentPreference;
        this.channels = channels;
        this.conditions = conditions;
        this.externalServiceConditions = externalServiceConditions;
        this.nrqlConditions = nrqlConditions;
    }

    public static PolicyConfigurationBuilder builder() {
        return new PolicyConfigurationBuilder();
    }

    public enum IncidentPreference {
        PER_POLICY, PER_CONDITION, PER_CONDITION_AND_TARGET
    }

    public static class PolicyConfigurationBuilder {
        private String policyName;
        private IncidentPreference incidentPreference;
        private ArrayList<Channel> channels;
        private ArrayList<Condition> conditions;
        private ArrayList<ExternalServiceCondition> externalServiceConditions;
        private ArrayList<NrqlCondition> nrqlConditions;

        PolicyConfigurationBuilder() {
        }

        public PolicyConfigurationBuilder policyName(String policyName) {
            this.policyName = policyName;
            return this;
        }

        public PolicyConfigurationBuilder incidentPreference(IncidentPreference incidentPreference) {
            this.incidentPreference = incidentPreference;
            return this;
        }

        public PolicyConfigurationBuilder channel(Channel channel) {
            if (this.channels == null) {
                this.channels = new ArrayList<>();
            }
            this.channels.add(channel);
            return this;
        }

        public PolicyConfigurationBuilder channels(Collection<? extends Channel> channels) {
            if (this.channels == null) {
                this.channels = new ArrayList<>();
            }
            this.channels.addAll(channels);
            return this;
        }

        public PolicyConfigurationBuilder clearChannels() {
            if (this.channels != null) {
                this.channels.clear();
            }

            return this;
        }

        public PolicyConfigurationBuilder condition(Condition condition) {
            if (this.conditions == null) {
                this.conditions = new ArrayList<>();
            }
            this.conditions.add(condition);
            return this;
        }

        public PolicyConfigurationBuilder conditions(Collection<? extends Condition> conditions) {
            if (this.conditions == null) {
                this.conditions = new ArrayList<>();
            }
            this.conditions.addAll(conditions);
            return this;
        }

        public PolicyConfigurationBuilder clearConditions() {
            if (this.conditions != null) {
                this.conditions.clear();
            }

            return this;
        }

        public PolicyConfigurationBuilder externalServiceCondition(
                ExternalServiceCondition externalServiceCondition) {
            if (this.externalServiceConditions == null) {
                this.externalServiceConditions = new ArrayList<>();
            }
            this.externalServiceConditions.add(externalServiceCondition);
            return this;
        }

        public PolicyConfigurationBuilder externalServiceConditions(
                Collection<? extends ExternalServiceCondition> externalServiceConditions) {
            if (this.externalServiceConditions == null) {
                this.externalServiceConditions = new ArrayList<>();
            }
            this.externalServiceConditions.addAll(externalServiceConditions);
            return this;
        }

        public PolicyConfigurationBuilder clearExternalServiceConditions() {
            if (this.externalServiceConditions != null) {
                this.externalServiceConditions.clear();
            }

            return this;
        }

        public PolicyConfigurationBuilder nrqlCondition(NrqlCondition nrqlCondition) {
            if (this.nrqlConditions == null) {
                this.nrqlConditions = new ArrayList<>();
            }
            this.nrqlConditions.add(nrqlCondition);
            return this;
        }

        public PolicyConfigurationBuilder nrqlConditions(Collection<? extends NrqlCondition> nrqlConditions) {
            if (this.nrqlConditions == null) {
                this.nrqlConditions = new ArrayList<>();
            }
            this.nrqlConditions.addAll(nrqlConditions);
            return this;
        }

        public PolicyConfigurationBuilder clearNrqlConditions() {
            if (this.nrqlConditions != null) {
                this.nrqlConditions.clear();
            }

            return this;
        }

        public PolicyConfiguration build() {
            return new PolicyConfiguration(
                    policyName,
                    incidentPreference,
                    normalizeList(this.channels),
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

        public String toString() {
            return "PolicyConfiguration.PolicyConfigurationBuilder(policyName=" + this.policyName + ", incidentPreference=" +
                    this.incidentPreference + ", channels=" + this.channels + ", conditions=" + this.conditions +
                    ", externalServiceConditions=" + this.externalServiceConditions + ", nrqlConditions=" + this.nrqlConditions +
                    ")";
        }
    }
}
