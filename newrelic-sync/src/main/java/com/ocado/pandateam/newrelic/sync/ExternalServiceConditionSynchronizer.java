package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.model.applications.Application;
import com.ocado.pandateam.newrelic.api.model.conditions.external.AlertsExternalServiceCondition;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.sync.configuration.ExternalServiceConditionConfiguration;
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
    private final ExternalServiceConditionConfiguration config;

    ExternalServiceConditionSynchronizer(@NonNull NewRelicApi api, @NonNull ExternalServiceConditionConfiguration config) {
        this.api = api;
        this.config = config;
    }

    void sync() {
        LOG.info(format("Synchronizing external service alerts conditions for policy %s...", config.getPolicyName()));

        Optional<AlertsPolicy> policyOptional = api.getAlertsPoliciesApi().getByName(config.getPolicyName());
        AlertsPolicy policy = policyOptional.orElseThrow(
            () -> new NewRelicSyncException(format("Policy %s does not exist", config.getPolicyName())));


        List<AlertsExternalServiceCondition> allAlertsExternalServiceConditions = api.getAlertsExternalServiceConditionsApi()
            .list(policy.getId());
        List<Integer> updatedAlertsExternalServiceConditionsIds = createOrUpdateAlertsExternalServiceConditions(
            policy, allAlertsExternalServiceConditions);

        cleanupOldAlertsExternalServiceConditions(policy, allAlertsExternalServiceConditions,
            updatedAlertsExternalServiceConditionsIds);
        LOG.info(format("External service alerts conditions for policy %s synchronized!", config.getPolicyName()));
    }

    private List<Integer> createOrUpdateAlertsExternalServiceConditions(AlertsPolicy policy,
                                                                        List<AlertsExternalServiceCondition> allAlertsConditions) {
        List<AlertsExternalServiceCondition> alertsConditionsFromConfig = config.getExternalServiceConditions().stream()
            .map(this::createAlertsExternalServiceCondition)
            .collect(Collectors.toList());

        List<AlertsExternalServiceCondition> updatedAlertConditions = new LinkedList<>();
        alertsConditionsFromConfig.stream().forEach(
            alertConditionFromConfig -> {
                Optional<AlertsExternalServiceCondition> alertsConditionToUpdate = allAlertsConditions.stream()
                    .filter(alertCondition -> sameInstance(alertCondition, alertConditionFromConfig))
                    .findFirst();
                if (alertsConditionToUpdate.isPresent()) {
                    AlertsExternalServiceCondition updatedCondition = api.getAlertsExternalServiceConditionsApi().update(
                        alertsConditionToUpdate.get().getId(), alertConditionFromConfig);
                    LOG.info(format("External service alerts condition %s (id: %d) updated for policy %s (id: %d)",
                        updatedCondition.getName(), updatedCondition.getId(), policy.getName(), policy.getId()));
                    updatedAlertConditions.add(updatedCondition);
                } else {
                    AlertsExternalServiceCondition newCondition = api.getAlertsExternalServiceConditionsApi().create(
                        policy.getId(), alertConditionFromConfig);
                    LOG.info(format("External service alerts condition %s (id: %d) created for policy %s (id: %d)",
                        newCondition.getName(), newCondition.getId(), policy.getName(), policy.getId()));
                }
            }
        );

        return updatedAlertConditions.stream()
            .map(AlertsExternalServiceCondition::getId)
            .collect(Collectors.toList());
    }

    private void cleanupOldAlertsExternalServiceConditions(AlertsPolicy policy,
                                                           List<AlertsExternalServiceCondition> allAlertsExternalServiceConditions,
                                                           List<Integer> updatedAlertsExternalServiceConditionsIds) {
        allAlertsExternalServiceConditions.stream()
            .filter(alertsCondition -> !updatedAlertsExternalServiceConditionsIds.contains(alertsCondition.getId()))
            .forEach(
                alertsCondition -> {
                    api.getAlertsExternalServiceConditionsApi().delete(alertsCondition.getId());
                    LOG.info(format("External service alerts condition %s (id: %d) removed from policy %s (id: %d)",
                        alertsCondition.getName(), alertsCondition.getId(), policy.getName(), policy.getId()));
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
