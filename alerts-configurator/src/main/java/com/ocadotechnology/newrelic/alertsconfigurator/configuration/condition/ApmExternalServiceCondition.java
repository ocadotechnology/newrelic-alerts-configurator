package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsConfiguration;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Collection;

/**
 * APM external service condition configuration.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #conditionName}</li>
 *     <li>{@link #enabled} (optional)</li>
 *     <li>{@link #applications}</li>
 *     <li>{@link #externalServiceUrl}</li>
 *     <li>{@link #metric}</li>
 *     <li>{@link #runBookUrl} (optional)</li>
 *     <li>{@link #terms}</li>
 * </ul>
 */
@Getter
@Builder
public class ApmExternalServiceCondition implements ExternalServiceCondition {
    private final ExternalServiceConditionType type = ExternalServiceConditionType.APM;
    /**
     * Name of your APM external service condition
     */
    @NonNull
    private String conditionName;
    /**
     * If your APM external service condition is enabled. Default is false
     */
    private boolean enabled;
    /**
     * Collection of application names for which this condition is applied.
     * If application with given name does not exist exception will be thrown
     */
    @NonNull
    @Singular
    private Collection<String> applications;
    /**
     * URL of the external service to be monitored. This string must not include the protocol
     */
    @NonNull
    private String externalServiceUrl;
    /**
     * Metric used in given condition
     */
    @NonNull
    private Metric metric;
    /**
     * The runbook URL to display in notifications
     */
    private String runBookUrl;
    /**
     * Collection of terms used for alerts condition
     */
    @NonNull
    @Singular
    private Collection<TermsConfiguration> terms;

    @Override
    public String getTypeString() {
        return type.getTypeString();
    }

    @Override
    public Collection<String> getEntities() {
        return getApplications();
    }

    @Override
    public String getMetric() {
        return metric.name().toLowerCase();
    }

    public enum Metric {
        RESPONSE_TIME_AVERAGE, RESPONSE_TIME_MINIMUM, RESPONSE_TIME_MAXIMUM, THROUGHPUT
    }
}
