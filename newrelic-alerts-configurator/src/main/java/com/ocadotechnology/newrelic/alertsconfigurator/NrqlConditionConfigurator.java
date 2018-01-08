package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql.NrqlCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql.NrqlUtils;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsUtils;
import com.ocadotechnology.newrelic.alertsconfigurator.exception.NewRelicSyncException;
import com.ocadotechnology.newrelic.apiclient.NewRelicApi;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.AlertsNrqlCondition;
import com.ocadotechnology.newrelic.apiclient.model.policies.AlertsPolicy;
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
public class NrqlConditionConfigurator {
    private final NewRelicApi api;

    NrqlConditionConfigurator(@NonNull NewRelicApi api) {
        this.api = api;
    }

    void sync(@NonNull PolicyConfiguration config) {
        LOG.info("Synchronizing alerts NRQL conditions for policy {}...", config.getPolicyName());

        AlertsPolicy policy = api.getAlertsPoliciesApi().getByName(config.getPolicyName()).orElseThrow(
                () -> new NewRelicSyncException(format("Policy %s does not exist", config.getPolicyName())));

        List<AlertsNrqlCondition> allAlertsConditions = api.getAlertsNrqlConditionsApi().list(policy.getId());
        List<Integer> updatedAlertsConditionsIds = createOrUpdateAlertsConditions(
                policy, config.getNrqlConditions(), allAlertsConditions);

        cleanupOldAlertsConditions(policy, allAlertsConditions, updatedAlertsConditionsIds);
        LOG.info("Alerts NRQL conditions for policy {} synchronized", config.getPolicyName());
    }

    private List<Integer> createOrUpdateAlertsConditions(AlertsPolicy policy,
                                                         Collection<NrqlCondition> conditionsFromConfig,
                                                         Collection<AlertsNrqlCondition> allAlertsConditions) {
        List<AlertsNrqlCondition> updatedAlertConditions = new LinkedList<>();
        for (NrqlCondition conditionFromConfig : conditionsFromConfig) {
            AlertsNrqlCondition alertConditionFromConfig = toAlertsCondition(conditionFromConfig);
            Optional<AlertsNrqlCondition> alertsConditionToUpdate = findAlertsConditionToUpdate(allAlertsConditions,
                    alertConditionFromConfig);

            if (alertsConditionToUpdate.isPresent()) {
                AlertsNrqlCondition updatedCondition = updateAlertsCondition(policy, alertConditionFromConfig,
                        alertsConditionToUpdate.get());
                updatedAlertConditions.add(updatedCondition);
            } else {
                createAlertsCondition(policy, alertConditionFromConfig);
            }
        }

        return updatedAlertConditions.stream()
                .map(AlertsNrqlCondition::getId)
                .collect(Collectors.toList());
    }

    private Optional<AlertsNrqlCondition> findAlertsConditionToUpdate(Collection<AlertsNrqlCondition> allAlertsConditions,
                                                                  AlertsNrqlCondition alertConditionFromConfig) {
        return allAlertsConditions.stream()
                .filter(alertCondition -> sameInstance(alertCondition, alertConditionFromConfig))
                .findAny();
    }

    private void createAlertsCondition(AlertsPolicy policy, AlertsNrqlCondition alertConditionFromConfig) {
        AlertsNrqlCondition newCondition = api.getAlertsNrqlConditionsApi().create(
                policy.getId(), alertConditionFromConfig);
        LOG.info("Alerts NRQL condition {} (id: {}) created for policy {} (id: {})",
                newCondition.getName(), newCondition.getId(), policy.getName(), policy.getId());
    }

    private AlertsNrqlCondition updateAlertsCondition(AlertsPolicy policy, AlertsNrqlCondition alertConditionFromConfig,
                                                  AlertsNrqlCondition alertsConditionToUpdate) {
        AlertsNrqlCondition updatedCondition = api.getAlertsNrqlConditionsApi().update(
                alertsConditionToUpdate.getId(), alertConditionFromConfig);
        LOG.info("Alerts condition {} (id: {}) updated for policy {} (id: {})",
                updatedCondition.getName(), updatedCondition.getId(), policy.getName(), policy.getId());
        return updatedCondition;
    }

    private void cleanupOldAlertsConditions(AlertsPolicy policy, List<AlertsNrqlCondition> allAlertsConditions,
                                            Collection<Integer> updatedAlertsConditionsIds) {
        allAlertsConditions.stream()
                .filter(alertsCondition -> !updatedAlertsConditionsIds.contains(alertsCondition.getId()))
                .forEach(
                        alertsCondition -> {
                            api.getAlertsNrqlConditionsApi().delete(alertsCondition.getId());
                            LOG.info("Alerts condition {} (id: {}) removed from policy {} (id: {})",
                                    alertsCondition.getName(), alertsCondition.getId(), policy.getName(), policy.getId());
                        }
                );
    }

    private AlertsNrqlCondition toAlertsCondition(NrqlCondition condition) {
        return AlertsNrqlCondition.builder()
                .name(condition.getConditionName())
                .enabled(condition.isEnabled())
                .runbookUrl(condition.getRunBookUrl())
                .terms(TermsUtils.createTerms(condition.getTerms()))
                .valueFunction(condition.getValueFunction().getValueString())
                .nrql(NrqlUtils.createNrql(condition.getNrql()))
                .build();
    }

    private static boolean sameInstance(AlertsNrqlCondition alertsCondition1, AlertsNrqlCondition alertsCondition2) {
        return StringUtils.equals(alertsCondition1.getName(), alertsCondition2.getName());
    }
}
