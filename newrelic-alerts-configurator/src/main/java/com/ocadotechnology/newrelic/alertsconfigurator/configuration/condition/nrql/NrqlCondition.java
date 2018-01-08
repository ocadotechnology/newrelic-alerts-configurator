package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ConditionType;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsConfiguration;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Collection;

/**
 * Alerts NRQL condition configuration.
 */
@Getter
@Builder
public class NrqlCondition {
    private final ConditionType type = ConditionType.SERVERS_METRIC;
    /**
     * Name of your servers metric condition.
     */
    @NonNull
    private String conditionName;
    /**
     * The runbook URL to display in notifications.
     */
    private String runBookUrl;
    /**
     * If your servers metric condition is enabled. Default is false.
     */
    private boolean enabled;

    /**
     * Collection of terms used for alerts condition.
     */
    @NonNull
    @Singular
    private Collection<TermsConfiguration> terms;

    /**
     * Function to be applied to the value returned by query
     */
    @NonNull
    private ValueFunction valueFunction;

    /**
     * NRQL
     */
    @NonNull
    private NrqlConfiguration nrql;


    public enum ValueFunction {
        SINGLE_VALUE, SUM;

        public String getAsString() {
            return this.name().toLowerCase();
        }
    }
}
