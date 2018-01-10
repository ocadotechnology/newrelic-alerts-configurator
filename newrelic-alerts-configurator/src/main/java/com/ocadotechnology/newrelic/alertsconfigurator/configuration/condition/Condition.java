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
     * Returns collection of entities for which this condition is applied.
     * The type of the entity depends on concrete condition implementation. This can be an application
     * name or host name for example.
     * <p>
     * If entity with given name does not exist an exception will be thrown.
     *
     * @return entity names
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
     * Returns time (in hours) after which instance-based violations will automatically close.
     * @return violation close timer
     */
    default String getViolationCloseTimerAsString() {
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

    /**
     * Returns type of GC metric for jvm condition if metric set to GC_CPU_TIME.
     *
     * @return type of GC metric
     */
    default String getGcMetricAsString() {
        return null;
    }

}
