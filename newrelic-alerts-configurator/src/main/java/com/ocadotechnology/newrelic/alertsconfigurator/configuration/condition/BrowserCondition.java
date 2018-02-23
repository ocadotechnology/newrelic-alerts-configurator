package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsConfiguration;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Collection;

/**
 * New Relic Browser configuration.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #conditionName}</li>
 *     <li>{@link #enabled}</li>
 *     <li>{@link #applications}</li>
 *     <li>{@link #metric}</li>
 *     <li>{@link #runBookUrl}</li>
 *     <li>{@link #terms}</li>
 *     <li>{@link #userDefinedConfiguration}</li>
 * </ul>
 */
@Getter
@Builder
public class BrowserCondition implements Condition {
    private final ConditionType type = ConditionType.BROWSER_METRIC;
    /**
     * Name of your New Relic Browser metric condition
     */
    @NonNull
    private String conditionName;
    /**
     * If your New Relic Browser metric condition is enabled. Default is true
     */
    @Builder.Default
    private boolean enabled = true;
    /**
     * Collection of application names for which this condition is applied.
     * If application with given name does not exist exception will be thrown
     */
    @NonNull
    @Singular
    private Collection<String> applications;
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
    /**
     * Configuration for user defined metric. Should be provided when {@link #metric} is set to USER_DEFINED
     */
    private UserDefinedConfiguration userDefinedConfiguration;

    @Override
    public Collection<String> getEntities() {
        return getApplications();
    }

    @Override
    public String getMetricAsString() {
        return metric.name().toLowerCase();
    }

    @Override
    public UserDefinedConfiguration getUserDefinedMetric() {
        return userDefinedConfiguration;
    }

    public enum Metric {
        END_USER_APDEX, TOTAL_PAGE_LOAD, PAGE_RENDERING, WEB_APPLICATION, NETWORK, DOM_PROCESSING, REQUEST_QUEUING,
        AJAX_RESPONSE_TIME, PAGE_VIEWS_WITH_JS_ERRORS, PAGE_VIEW_THROUGHPUT, AJAX_THROUGHPUT, USER_DEFINED
    }
}
