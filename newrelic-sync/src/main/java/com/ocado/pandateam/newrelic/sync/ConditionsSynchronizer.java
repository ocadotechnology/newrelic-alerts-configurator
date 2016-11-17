package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.applications.Application;
import com.ocado.pandateam.newrelic.api.model.conditions.AlertsCondition;
import com.ocado.pandateam.newrelic.api.model.conditions.Terms;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.sync.configuration.ConditionsConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.condition.Condition;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.TermsConfiguration;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class ConditionsSynchronizer {

    private final NewRelicApi api;
    private final ConditionsConfiguration config;

    public ConditionsSynchronizer(NewRelicApi api, ConditionsConfiguration config) {
        this.api = api;
        this.config = config;
    }

    public void sync() throws NewRelicApiException, NewRelicSyncException {
        Optional<AlertsPolicy> policyOptional = api.getAlertsPoliciesApi().getByName(config.getPolicyName());
        AlertsPolicy policy = policyOptional.orElseThrow(
            () -> new NewRelicSyncException(format("Policy %s does not exist", config.getPolicyName())));


        List<AlertsCondition> alertConditions = api.getAlertsConditionsApi().list(policy.getId());
        List<AlertsCondition> alertConditionsFromList = config.getConditions().stream()
            .map(this::createAlertsCondition)
            .collect(Collectors.toList());
    }

    private AlertsCondition createAlertsCondition(Condition condition) {
        return AlertsCondition.builder()
            .type(condition.getType())
            .name(condition.getConditionName())
            .enabled(condition.isEnabled())
            .entities(getEntities(condition.getEntities()))
            .metric(condition.getMetric())
            .conditionScope(condition.getConditionScope())
            .runbookUrl(condition.getRunBookUrl())
            .terms(createTerms(condition))
            .build();
    }

    private Collection<Integer> getEntities(Collection<String> entities) {
        return entities.stream()
            .map(
                entity -> {
                    Optional<Application> applicationOptional = api.getApplicationsApi().getByName(entity);
                    Application application = applicationOptional.orElseThrow(
                        () -> new NewRelicSyncException(format("Application %s does not exist", entity)));
                    return application.getId();
                }
            )
            .collect(Collectors.toList());
    }

    private Collection<Terms> createTerms(Condition condition) {
        return condition.getTerms().stream().map(this::mapTerms).collect(Collectors.toList());
    }

    private Terms mapTerms(TermsConfiguration termsConfiguration) {
        return Terms.builder()
            .duration(termsConfiguration.getDurationTerm())
            .operator(termsConfiguration.getOperatorTerm())
            .priority(termsConfiguration.getPriorityTerm())
            .threshold(termsConfiguration.getThresholdTerm())
            .timeFunction(termsConfiguration.getTimeFunctionTerm())
            .build();
    }
}
