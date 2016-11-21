package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.model.conditions.AlertsCondition;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.sync.configuration.PolicyConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.condition.Condition;
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
class ConditionSynchronizer {
    private final NewRelicApi api;
    private final EntityIdProvider entityIdProvider;

    ConditionSynchronizer(@NonNull NewRelicApi api) {
        this.api = api;
        this.entityIdProvider = new EntityIdProvider(api);
    }

    void sync(@NonNull PolicyConfiguration config) {
        LOG.info("Synchronizing alerts conditions for policy {}...", config.getPolicyName());

        AlertsPolicy policy = api.getAlertsPoliciesApi().getByName(config.getPolicyName()).orElseThrow(
            () -> new NewRelicSyncException(format("Policy %s does not exist", config.getPolicyName())));

        List<AlertsCondition> allAlertsConditions = api.getAlertsConditionsApi().list(policy.getId());
        List<Integer> updatedAlertsConditionsIds = createOrUpdateAlertsConditions(
            policy, config.getConditions(), allAlertsConditions);

        cleanupOldAlertsConditions(policy, allAlertsConditions, updatedAlertsConditionsIds);
        LOG.info("Alerts conditions for policy {} synchronized", config.getPolicyName());
    }

    private List<Integer> createOrUpdateAlertsConditions(AlertsPolicy policy,
                                                         Collection<Condition> conditionsFromConfig,
                                                         Collection<AlertsCondition> allAlertsConditions) {
        List<AlertsCondition> updatedAlertConditions = new LinkedList<>();
        for (Condition conditionFromConfig : conditionsFromConfig) {
            AlertsCondition alertConditionFromConfig = toAlertsCondition(conditionFromConfig);
            Optional<AlertsCondition> alertsConditionToUpdate = findAlertsConditionToUpdate(allAlertsConditions,
                alertConditionFromConfig);

            if (alertsConditionToUpdate.isPresent()) {
                AlertsCondition updatedCondition = updateAlertsCondition(policy, alertConditionFromConfig,
                    alertsConditionToUpdate.get());
                updatedAlertConditions.add(updatedCondition);
            } else {
                createAlertsCondition(policy, alertConditionFromConfig);
            }
        }

        return updatedAlertConditions.stream()
            .map(AlertsCondition::getId)
            .collect(Collectors.toList());
    }

    private Optional<AlertsCondition> findAlertsConditionToUpdate(Collection<AlertsCondition> allAlertsConditions,
                                                                  AlertsCondition alertConditionFromConfig) {
        return allAlertsConditions.stream()
            .filter(alertCondition -> sameInstance(alertCondition, alertConditionFromConfig))
            .findAny();
    }

    private void createAlertsCondition(AlertsPolicy policy, AlertsCondition alertConditionFromConfig) {
        AlertsCondition newCondition = api.getAlertsConditionsApi().create(
            policy.getId(), alertConditionFromConfig);
        LOG.info("Alerts condition {} (id: {}) created for policy {} (id: {})",
            newCondition.getName(), newCondition.getId(), policy.getName(), policy.getId());
    }

    private AlertsCondition updateAlertsCondition(AlertsPolicy policy, AlertsCondition alertConditionFromConfig,
                                                  AlertsCondition alertsConditionToUpdate) {
        AlertsCondition updatedCondition = api.getAlertsConditionsApi().update(
            alertsConditionToUpdate.getId(), alertConditionFromConfig);
        LOG.info("Alerts condition {} (id: {}) updated for policy {} (id: {})",
            updatedCondition.getName(), updatedCondition.getId(), policy.getName(), policy.getId());
        return updatedCondition;
    }

    private void cleanupOldAlertsConditions(AlertsPolicy policy, List<AlertsCondition> allAlertsConditions,
                                            Collection<Integer> updatedAlertsConditionsIds) {
        allAlertsConditions.stream()
            .filter(alertsCondition -> !updatedAlertsConditionsIds.contains(alertsCondition.getId()))
            .forEach(
                alertsCondition -> {
                    api.getAlertsConditionsApi().delete(alertsCondition.getId());
                    LOG.info("Alerts condition {} (id: {}) removed from policy {} (id: {})",
                        alertsCondition.getName(), alertsCondition.getId(), policy.getName(), policy.getId());
                }
            );
    }

    private AlertsCondition toAlertsCondition(Condition condition) {
        return AlertsCondition.builder()
            .type(condition.getType().getTypeString())
            .name(condition.getConditionName())
            .enabled(condition.isEnabled())
            .entities(getEntities(condition))
            .metric(condition.getMetricAsString())
            .conditionScope(condition.getConditionScopeAsString())
            .runbookUrl(condition.getRunBookUrl())
            .terms(TermsUtils.createTerms(condition.getTerms()))
            .build();
    }

    private Collection<Integer> getEntities(Condition condition) {
        switch (condition.getType()) {
            case APM_APP:
                return condition.getEntities().stream()
                    .map(entityIdProvider::getApplicationId)
                    .collect(Collectors.toList());
            case APM_KEY_TRANSACTION:
                return condition.getEntities().stream()
                    .map(entityIdProvider::getKeyTransactionId)
                    .collect(Collectors.toList());
            default:
                throw new NewRelicSyncException(format("Could not get entities for condition %s", condition.getConditionName()));
        }
    }

    private static boolean sameInstance(AlertsCondition alertsCondition1, AlertsCondition alertsCondition2) {
        return StringUtils.equals(alertsCondition1.getName(), alertsCondition2.getName())
            && StringUtils.equals(alertsCondition1.getType(), alertsCondition2.getType());
    }
}
