package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql.NrqlCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql.NrqlUtils;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsUtils;
import com.ocadotechnology.newrelic.alertsconfigurator.exception.NewRelicSyncException;
import com.ocadotechnology.newrelic.apiclient.NewRelicApi;

import com.ocadotechnology.newrelic.apiclient.model.conditions.AlertsNrqlCondition;
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
class NrqlConditionConfigurator {
    private final NewRelicApi api;

    NrqlConditionConfigurator(@NonNull NewRelicApi api) {
        this.api = api;
    }

    void sync(@NonNull PolicyConfiguration config) {
        LOG.info("Synchronizing alerts NrqlConditions for policy {}...", config.getPolicyName());

        AlertsPolicy policy = api.getAlertsPoliciesApi().getByName(config.getPolicyName()).orElseThrow(
                () -> new NewRelicSyncException(format("Policy %s does not exist", config.getPolicyName())));

        List<AlertsNrqlCondition> allAlertsNrqlConditions = api.getAlertsNrqlConditionsApi().list(policy.getId());
        List<Integer> updatedAlertsNrqlConditionsIds = createOrUpdateAlertsNrqlConditions(
                policy, config.getNrqlConditions(), allAlertsNrqlConditions);

        cleanupOldAlertsNrqlConditions(policy, allAlertsNrqlConditions, updatedAlertsNrqlConditionsIds);
        LOG.info("Alerts NrqlConditions for policy {} synchronized", config.getPolicyName());
    }

    private List<Integer> createOrUpdateAlertsNrqlConditions(AlertsPolicy policy,
                                                         Collection<NrqlCondition> NrqlConditionsFromConfig,
                                                         Collection<AlertsNrqlCondition> allAlertsNrqlConditions) {
        List<AlertsNrqlCondition> updatedAlertNrqlConditions = new LinkedList<>();
        for (NrqlCondition NrqlConditionFromConfig : NrqlConditionsFromConfig) {
            AlertsNrqlCondition alertNrqlConditionFromConfig = toAlertsNrqlCondition(NrqlConditionFromConfig);
            Optional<AlertsNrqlCondition> alertsNrqlConditionToUpdate = findAlertsNrqlConditionToUpdate(allAlertsNrqlConditions,
                    alertNrqlConditionFromConfig);

            if (alertsNrqlConditionToUpdate.isPresent()) {
                AlertsNrqlCondition updatedNrqlCondition = updateAlertsNrqlCondition(policy, alertNrqlConditionFromConfig,
                        alertsNrqlConditionToUpdate.get());
                updatedAlertNrqlConditions.add(updatedNrqlCondition);
            } else {
                createAlertsNrqlCondition(policy, alertNrqlConditionFromConfig);
            }
        }

        return updatedAlertNrqlConditions.stream()
                .map(AlertsNrqlCondition::getId)
                .collect(Collectors.toList());
    }

    private Optional<AlertsNrqlCondition> findAlertsNrqlConditionToUpdate(Collection<AlertsNrqlCondition> allAlertsNrqlConditions,
                                                                  AlertsNrqlCondition alertNrqlConditionFromConfig) {
        return allAlertsNrqlConditions.stream()
                .filter(alertNrqlCondition -> sameInstance(alertNrqlCondition, alertNrqlConditionFromConfig))
                .findAny();
    }

    private void createAlertsNrqlCondition(AlertsPolicy policy, AlertsNrqlCondition alertNrqlConditionFromConfig) {
        AlertsNrqlCondition newNrqlCondition = api.getAlertsNrqlConditionsApi().create(
                policy.getId(), alertNrqlConditionFromConfig);
        LOG.info("Alerts NrqlCondition {} (id: {}) created for policy {} (id: {})",
                newNrqlCondition.getName(), newNrqlCondition.getId(), policy.getName(), policy.getId());
    }

    private AlertsNrqlCondition updateAlertsNrqlCondition(AlertsPolicy policy, AlertsNrqlCondition alertNrqlConditionFromConfig,
                                                  AlertsNrqlCondition alertsNrqlConditionToUpdate) {
        AlertsNrqlCondition updatedNrqlCondition = api.getAlertsNrqlConditionsApi().update(
                alertsNrqlConditionToUpdate.getId(), alertNrqlConditionFromConfig);
        LOG.info("Alerts NrqlCondition {} (id: {}) updated for policy {} (id: {})",
                updatedNrqlCondition.getName(), updatedNrqlCondition.getId(), policy.getName(), policy.getId());
        return updatedNrqlCondition;
    }

    private void cleanupOldAlertsNrqlConditions(AlertsPolicy policy, List<AlertsNrqlCondition> allAlertsNrqlConditions,
                                            Collection<Integer> updatedAlertsNrqlConditionsIds) {
        allAlertsNrqlConditions.stream()
                .filter(alertsNrqlCondition -> !updatedAlertsNrqlConditionsIds.contains(alertsNrqlCondition.getId()))
                .forEach(
                        alertsNrqlCondition -> {
                            api.getAlertsNrqlConditionsApi().delete(alertsNrqlCondition.getId());
                            LOG.info("Alerts NrqlCondition {} (id: {}) removed from policy {} (id: {})",
                                    alertsNrqlCondition.getName(), alertsNrqlCondition.getId(), policy.getName(), policy.getId());
                        }
                );
    }

    private AlertsNrqlCondition toAlertsNrqlCondition(NrqlCondition nrqlCondition) {
        return AlertsNrqlCondition.builder()
                .name(nrqlCondition.getConditionName())
                .enabled(nrqlCondition.isEnabled())
                .runbookUrl(nrqlCondition.getRunBookUrl())
                .terms(TermsUtils.createTerms(nrqlCondition.getTerms()))
                .valueFunction(nrqlCondition.getValueFunction().getAsString())
                .nrql(NrqlUtils.mapNrql(nrqlCondition.getNrql()))
                .build();
    }

    private static boolean sameInstance(AlertsNrqlCondition alertsNrqlCondition1, AlertsNrqlCondition alertsNrqlCondition2) {
        return StringUtils.equals(alertsNrqlCondition1.getName(), alertsNrqlCondition2.getName());
    }
}
