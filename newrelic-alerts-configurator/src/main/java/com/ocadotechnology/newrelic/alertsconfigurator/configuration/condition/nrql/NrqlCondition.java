package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.NrqlTermsConfiguration;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Collection;

/**
 * NRQL condition.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #conditionName}</li>
 *     <li>{@link #enabled} (optional)</li>
 *     <li>{@link #runBookUrl} (optional)</li>
 *     <li>{@link #terms}</li>
 *     <li>{@link #valueFunction}</li>
 *     <li>{@link #nrql}</li>
 * </ul>
 */
@Getter
@Builder
public class NrqlCondition {
    /**
     * Name of your NRQL condition
     */
    @NonNull
    private String conditionName;
    /**
     * If your NRQL condition is enabled. Default is false
     */
    private boolean enabled;
    /**
     * The runbook URL to display in notifications
     */
    private String runBookUrl;
    /**
     * Collection of terms used for alerts condition
     */
    @NonNull
    @Singular
    private Collection<NrqlTermsConfiguration> terms;
    /**
     * Value function
     */
    @NonNull
    private ValueFunction valueFunction;
    /**
     * Configuration for New Relic Query Language.
     */
    @NonNull
    private NrqlConfiguration nrql;
}
