package com.ocadotechnology.newrelic.alertconfiguploader.configuration.condition;

import com.ocadotechnology.newrelic.alertconfiguploader.configuration.condition.terms.TermsConfiguration;

import java.util.Collection;

/**
 * Alerts external service condition configuration.
 * Implementations:
 * <ul>
 *     <li>{@link ApmExternalServiceCondition}</li>
 * </ul>
 */
public interface ExternalServiceCondition {
    ExternalServiceConditionType getType();
    String getTypeString();
    String getConditionName();
    boolean isEnabled();
    Collection<String> getEntities();
    String getExternalServiceUrl();
    String getMetric();
    String getRunBookUrl();
    Collection<TermsConfiguration> getTerms();
}
