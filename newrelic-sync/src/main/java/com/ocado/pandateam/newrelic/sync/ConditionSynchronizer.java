package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.model.applications.Application;
import com.ocado.pandateam.newrelic.api.model.conditions.AlertsCondition;
import com.ocado.pandateam.newrelic.api.model.conditions.Terms;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.sync.configuration.ConditionConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.condition.Condition;
import com.ocado.pandateam.newrelic.sync.configuration.condition.ConditionUtils;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.TermsConfiguration;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;
import lombok.NonNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.String.format;

class ConditionSynchronizer {
    private static final Logger LOG = Logger.getLogger(ConditionSynchronizer.class.getName());

    private final NewRelicApi api;
    private final ConditionConfiguration config;

    ConditionSynchronizer(@NonNull NewRelicApi api, @NonNull ConditionConfiguration config) {
        this.api = api;
        this.config = config;
    }

    void sync() {
        LOG.info(format("Synchronizing conditions for policy %s", config.getPolicyName()));

        Optional<AlertsPolicy> policyOptional = api.getAlertsPoliciesApi().getByName(config.getPolicyName());
        AlertsPolicy policy = policyOptional.orElseThrow(
            () -> new NewRelicSyncException(format("Policy %s does not exist", config.getPolicyName())));


        List<AlertsCondition> allAlertsConditions = api.getAlertsConditionsApi().list(policy.getId());
        List<Integer> updatedAlertsConditionsIds = createOrUpdateAlertsConditions(policy, allAlertsConditions);

        cleanupOldAlertsConditions(policy, allAlertsConditions, updatedAlertsConditionsIds);
        LOG.info(format("Conditions for policy %s synchronized!", config.getPolicyName()));
    }

    private List<Integer> createOrUpdateAlertsConditions(AlertsPolicy policy,
                                                         List<AlertsCondition> allAlertsConditions) {

        List<AlertsCondition> alertsConditionsFromConfig = config.getConditions().stream()
            .map(this::createAlertsCondition)
            .collect(Collectors.toList());

        List<AlertsCondition> updatedAlertConditions = new LinkedList<>();
        alertsConditionsFromConfig.stream().forEach(
            alertConditionFromConfig -> {
                Optional<AlertsCondition> alertsConditionToUpdate = allAlertsConditions.stream()
                    .filter(alertCondition -> ConditionUtils.sameInstance(alertCondition, alertConditionFromConfig))
                    .findFirst();
                if (alertsConditionToUpdate.isPresent()) {
                    AlertsCondition updatedCondition = api.getAlertsConditionsApi().update(
                        alertsConditionToUpdate.get().getId(), alertConditionFromConfig);
                    LOG.info(format("Alert condition %s (id: %d) updated for policy %s (id: %d)",
                        updatedCondition.getName(), updatedCondition.getId(), policy.getName(), policy.getId()));
                    updatedAlertConditions.add(updatedCondition);
                } else {
                    AlertsCondition newCondition = api.getAlertsConditionsApi().create(
                        policy.getId(), alertConditionFromConfig);
                    LOG.info(format("Alert condition %s (id: %d) created for policy %s (id: %d)",
                        newCondition.getName(), newCondition.getId(), policy.getName(), policy.getId()));
                }
            }
        );
        return updatedAlertConditions.stream()
            .map(AlertsCondition::getId)
            .collect(Collectors.toList());
    }

    private void cleanupOldAlertsConditions(AlertsPolicy policy, List<AlertsCondition> allAlertsConditions,
                                            List<Integer> updatedAlertsConditionsIds) {
        allAlertsConditions.stream()
            .filter(alertsCondition -> !updatedAlertsConditionsIds.contains(alertsCondition.getId()))
            .forEach(
                alertsCondition -> {
                    api.getAlertsConditionsApi().delete(alertsCondition.getId());
                    LOG.info(format("Alert condition %s (id: %d) removed from policy %s (id: %d)",
                        alertsCondition.getName(), alertsCondition.getId(), policy.getName(), policy.getId()));
                }
            );
    }

    private AlertsCondition createAlertsCondition(Condition condition) {
        return AlertsCondition.builder()
            .type(condition.getTypeString())
            .name(condition.getConditionName())
            .enabled(condition.isEnabled())
            .entities(getEntities(condition))
            .metric(condition.getMetric())
            .conditionScope(condition.getConditionScope())
            .runbookUrl(condition.getRunBookUrl())
            .terms(createTerms(condition))
            .build();
    }

    private Collection<Integer> getEntities(Condition condition) {
        switch (condition.getType()) {
            case APM_APP:
                return condition.getEntities().stream()
                    .map(
                        entity -> {
                            Optional<Application> applicationOptional = api.getApplicationsApi().getByName(entity);
                            Application application = applicationOptional.orElseThrow(
                                () -> new NewRelicSyncException(format("Application %s does not exist", entity)));
                            return application.getId();
                        }
                    )
                    .collect(Collectors.toList());
            default:
                throw new NewRelicSyncException(format("Could not get entities for condition %s", condition.getConditionName()));
        }
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
