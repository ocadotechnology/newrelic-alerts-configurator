package com.ocado.pandateam.newrelic.sync.configuration.condition;

import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.TermsConfiguration;

import java.util.Collection;

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
