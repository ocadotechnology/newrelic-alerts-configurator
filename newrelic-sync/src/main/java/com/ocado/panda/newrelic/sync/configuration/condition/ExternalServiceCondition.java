package com.ocado.panda.newrelic.sync.configuration.condition;

import com.ocado.panda.newrelic.sync.configuration.condition.terms.TermsConfiguration;

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
