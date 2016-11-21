package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.model.conditions.external.AlertsExternalServiceCondition;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.sync.configuration.PolicyConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.condition.ExternalServiceCondition;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.TermsUtils;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;
import com.ocado.pandateam.newrelic.sync.internal.EntityIdProvider;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
class ExternalServiceConditionSynchronizer {
    private final NewRelicApi api;
    private final EntityIdProvider entityIdProvider;

    ExternalServiceConditionSynchronizer(@NonNull NewRelicApi api) {
        this.api = api;
        this.entityIdProvider = new EntityIdProvider(api);
    }

    void sync(@NonNull PolicyConfiguration config) {
        LOG.info("Synchronizing external service alerts conditions for policy {}...", config.getPolicyName());

        AlertsPolicy policy = api.getAlertsPoliciesApi().getByName(config.getPolicyName()).orElseThrow(
            () -> new NewRelicSyncException(format("Policy %s does not exist", config.getPolicyName())));


        List<AlertsExternalServiceCondition> allAlertsConditions = api.getAlertsExternalServiceConditionsApi()
            .list(policy.getId());
        List<Integer> updatedAlertsConditionsIds = createOrUpdateAlertsConditions(
            policy, config.getExternalServiceConditions(), allAlertsConditions);

        cleanupOldAlertsConditions(policy, allAlertsConditions, updatedAlertsConditionsIds);
        LOG.info("External service alerts conditions for policy {} synchronized", config.getPolicyName());
    }

    private List<Integer> createOrUpdateAlertsConditions(AlertsPolicy policy,
                                                         Collection<ExternalServiceCondition> conditionsFromConfig,
                                                         Collection<AlertsExternalServiceCondition> allAlertsConditions) {
        List<AlertsExternalServiceCondition> updatedAlertConditions = new LinkedList<>();
        for (ExternalServiceCondition alertsConditionFromConfig : conditionsFromConfig) {
            AlertsExternalServiceCondition alertConditionFromConfig = toAlertsExternalServiceCondition(alertsConditionFromConfig);
            Optional<AlertsExternalServiceCondition> alertsConditionToUpdate = findAlertsConditionToUpdate(
                allAlertsConditions, alertConditionFromConfig);

            if (alertsConditionToUpdate.isPresent()) {
                AlertsExternalServiceCondition updatedCondition = updateAlertsCondition(
                    policy, alertConditionFromConfig, alertsConditionToUpdate.get());
                updatedAlertConditions.add(updatedCondition);
            } else {
                createAlertsCondition(policy, alertConditionFromConfig);
            }
        }

        return updatedAlertConditions.stream()
            .map(AlertsExternalServiceCondition::getId)
            .collect(Collectors.toList());
    }

    private AlertsExternalServiceCondition updateAlertsCondition(AlertsPolicy policy,
                                                                 AlertsExternalServiceCondition alertsConditionFromConfig,
                                                                 AlertsExternalServiceCondition alertsConditionToUpdate) {
        AlertsExternalServiceCondition updatedCondition = api.getAlertsExternalServiceConditionsApi().update(
            alertsConditionToUpdate.getId(), alertsConditionFromConfig);
        LOG.info("External service alerts condition {} (id: {}) updated for policy {} (id: {})",
            updatedCondition.getName(), updatedCondition.getId(), policy.getName(), policy.getId());
        return updatedCondition;
    }

    private void createAlertsCondition(AlertsPolicy policy, AlertsExternalServiceCondition alertsConditionFromConfig) {
        AlertsExternalServiceCondition newCondition = api.getAlertsExternalServiceConditionsApi().create(
            policy.getId(), alertsConditionFromConfig);
        LOG.info("External service alerts condition {} (id: {}) created for policy {} (id: {})",
            newCondition.getName(), newCondition.getId(), policy.getName(), policy.getId());
    }

    private Optional<AlertsExternalServiceCondition> findAlertsConditionToUpdate(Collection<AlertsExternalServiceCondition> allAlertsConditions,
                                                                                 AlertsExternalServiceCondition alertsConditionFromConfig) {
        return allAlertsConditions.stream()
            .filter(alertCondition -> sameInstance(alertCondition, alertsConditionFromConfig))
            .findFirst();
    }

    private void cleanupOldAlertsConditions(AlertsPolicy policy,
                                            List<AlertsExternalServiceCondition> allAlertsConditions,
                                            List<Integer> updatedAlertsConditionsIds) {
        allAlertsConditions.stream()
            .filter(alertsCondition -> !updatedAlertsConditionsIds.contains(alertsCondition.getId()))
            .forEach(
                alertsCondition -> {
                    api.getAlertsExternalServiceConditionsApi().delete(alertsCondition.getId());
                    LOG.info("External service alerts condition {} (id: {}) removed from policy {} (id: {})",
                        alertsCondition.getName(), alertsCondition.getId(), policy.getName(), policy.getId());
                }
            );
    }

    private AlertsExternalServiceCondition toAlertsExternalServiceCondition(ExternalServiceCondition condition) {
        return AlertsExternalServiceCondition.builder()
            .type(condition.getTypeString())
            .name(condition.getConditionName())
            .enabled(condition.isEnabled())
            .entities(getEntities(condition))
            .metric(condition.getMetric())
            .externalServiceUrl(condition.getExternalServiceUrl())
            .runbookUrl(condition.getRunBookUrl())
            .terms(TermsUtils.createTerms(condition.getTerms()))
            .build();
    }

    private Collection<Integer> getEntities(ExternalServiceCondition condition) {
        switch (condition.getType()) {
            case APM:
                return condition.getEntities().stream()
                    .map(entityIdProvider::getApplicationId)
                    .collect(Collectors.toList());
            default:
                throw new NewRelicSyncException(format("Could not get entities for external service alerts condition %s",
                    condition.getConditionName()));
        }
    }

    private static boolean sameInstance(AlertsExternalServiceCondition alertsCondition1,
                                        AlertsExternalServiceCondition alertsCondition2) {
        return StringUtils.equals(alertsCondition1.getName(), alertsCondition2.getName())
            && StringUtils.equals(alertsCondition1.getType(), alertsCondition2.getType());
    }
}
