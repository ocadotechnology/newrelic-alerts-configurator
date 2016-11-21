package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.model.applications.Application;
import com.ocado.pandateam.newrelic.api.model.conditions.external.AlertsExternalServiceCondition;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.sync.configuration.PolicyConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.condition.ExternalServiceCondition;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.TermsUtils;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;
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

    ExternalServiceConditionSynchronizer(@NonNull NewRelicApi api) {
        this.api = api;
    }

    void sync(@NonNull PolicyConfiguration config) {
        LOG.info("Synchronizing external service alerts conditions for policy {}...", config.getPolicyName());

        AlertsPolicy policy = api.getAlertsPoliciesApi().getByName(config.getPolicyName()).orElseThrow(
            () -> new NewRelicSyncException(format("Policy %s does not exist", config.getPolicyName())));


        List<AlertsExternalServiceCondition> allAlertsExternalServiceConditions = api.getAlertsExternalServiceConditionsApi()
            .list(policy.getId());
        List<Integer> updatedAlertsExternalServiceConditionsIds = createOrUpdateAlertsExternalServiceConditions(
            policy, config.getExternalServiceConditions(), allAlertsExternalServiceConditions);

        cleanupOldAlertsExternalServiceConditions(policy, allAlertsExternalServiceConditions,
            updatedAlertsExternalServiceConditionsIds);
        LOG.info("External service alerts conditions for policy {} synchronized", config.getPolicyName());
    }

    private List<Integer> createOrUpdateAlertsExternalServiceConditions(AlertsPolicy policy,
                                                                        Collection<ExternalServiceCondition> conditionsFromConfig,
                                                                        Collection<AlertsExternalServiceCondition> allAlertsConditions) {
        List<AlertsExternalServiceCondition> updatedAlertConditions = new LinkedList<>();
        for (ExternalServiceCondition alertsConditionFromConfig : conditionsFromConfig) {
            AlertsExternalServiceCondition alertConditionFromConfig = createAlertsExternalServiceCondition(alertsConditionFromConfig);
            Optional<AlertsExternalServiceCondition> alertsConditionToUpdate = findAlertsExternalServiceConditionToUpdate(
                allAlertsConditions, alertConditionFromConfig);

            if (alertsConditionToUpdate.isPresent()) {
                AlertsExternalServiceCondition updatedCondition = updateAlertsExternalServiceCondition(
                    policy, alertConditionFromConfig, alertsConditionToUpdate.get());
                updatedAlertConditions.add(updatedCondition);
            } else {
                createAlertsExternalServiceCondition(policy, alertConditionFromConfig);
            }
        }

        return updatedAlertConditions.stream()
            .map(AlertsExternalServiceCondition::getId)
            .collect(Collectors.toList());
    }

    private AlertsExternalServiceCondition updateAlertsExternalServiceCondition(AlertsPolicy policy,
                                                                                AlertsExternalServiceCondition alertConditionFromConfig,
                                                                                AlertsExternalServiceCondition alertsConditionToUpdate) {
        AlertsExternalServiceCondition updatedCondition = api.getAlertsExternalServiceConditionsApi().update(
            alertsConditionToUpdate.getId(), alertConditionFromConfig);
        LOG.info("External service alerts condition {} (id: {}) updated for policy {} (id: {})",
            updatedCondition.getName(), updatedCondition.getId(), policy.getName(), policy.getId());
        return updatedCondition;
    }

    private void createAlertsExternalServiceCondition(AlertsPolicy policy, AlertsExternalServiceCondition alertConditionFromConfig) {
        AlertsExternalServiceCondition newCondition = api.getAlertsExternalServiceConditionsApi().create(
            policy.getId(), alertConditionFromConfig);
        LOG.info("External service alerts condition {} (id: {}) created for policy {} (id: {})",
            newCondition.getName(), newCondition.getId(), policy.getName(), policy.getId());
    }

    private Optional<AlertsExternalServiceCondition> findAlertsExternalServiceConditionToUpdate(Collection<AlertsExternalServiceCondition> allAlertsConditions,
                                                                                                AlertsExternalServiceCondition alertConditionFromConfig) {
        return allAlertsConditions.stream()
            .filter(alertCondition -> sameInstance(alertCondition, alertConditionFromConfig))
            .findFirst();
    }

    private void cleanupOldAlertsExternalServiceConditions(AlertsPolicy policy,
                                                           List<AlertsExternalServiceCondition> allAlertsExternalServiceConditions,
                                                           List<Integer> updatedAlertsExternalServiceConditionsIds) {
        allAlertsExternalServiceConditions.stream()
            .filter(alertsCondition -> !updatedAlertsExternalServiceConditionsIds.contains(alertsCondition.getId()))
            .forEach(
                alertsCondition -> {
                    api.getAlertsExternalServiceConditionsApi().delete(alertsCondition.getId());
                    LOG.info("External service alerts condition {} (id: {}) removed from policy {} (id: {})",
                        alertsCondition.getName(), alertsCondition.getId(), policy.getName(), policy.getId());
                }
            );
    }

    private AlertsExternalServiceCondition createAlertsExternalServiceCondition(ExternalServiceCondition condition) {
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
                throw new NewRelicSyncException(format("Could not get entities for external service alerts condition %s",
                    condition.getConditionName()));
        }
    }

    private static boolean sameInstance(AlertsExternalServiceCondition alertsExternalServiceCondition1,
                                        AlertsExternalServiceCondition alertsExternalServiceCondition2) {
        return StringUtils.equals(alertsExternalServiceCondition1.getName(), alertsExternalServiceCondition2.getName())
            && StringUtils.equals(alertsExternalServiceCondition1.getType(), alertsExternalServiceCondition2.getType());
    }
}
