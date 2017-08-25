package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsConfiguration;

import java.util.Collection;

/**
 * Alerts condition configuration.
 * Implementations:
 * <ul>
 *     <li>{@link ApmAppCondition}</li>
 *     <li>{@link ApmKeyTransactionCondition}</li>
 *     <li>{@link ServersMetricCondition}</li>
 * </ul>
 */
public interface Condition {
    /**
     * Returns condition type. Each Condition implementation should have unique type.
     *
     * @return condition type
     */
    ConditionType getType();

    /**
     * Returns name of the condition.
     *
     * @return condition name
     */
    String getConditionName();

    /**
     * Returns if condition is enabled.
     *
     * @return {@code true} if condition is enabled, {@code false} otherwise
     */
    boolean isEnabled();

    /**
     * Returns collection of application names for which this condition is applied.
     * If application with given name does not exist exception will be thrown
     *
     * @return application names
     */
    Collection<String> getEntities();

    /**
     * Returns metric in string format.
     *
     * @return metric
     */
    String getMetricAsString();

    /**
     * Returns the runbook URL to display in notifications.
     *
     * @return runbook URL
     */
    String getRunBookUrl();

    /**
     * Returns collection of terms used for alerts condition.
     *
     * @return terms
     */
    Collection<TermsConfiguration> getTerms();

    /**
     * Returns condition scope in string format.
     *
     * @return condition scope
     */
    default String getConditionScopeAsString() {
        return null;
    }

    /**
     * Returns configuration for user defined metric. Should be provided when metric is set to USER_DEFINED.
     *
     * @return user defined metric
     */
    default UserDefinedConfiguration getUserDefinedMetric() {
        return null;
    }

}
