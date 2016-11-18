package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.model.applications.Application;
import com.ocado.pandateam.newrelic.api.model.conditions.AlertsCondition;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.api.model.transactions.KeyTransaction;
import com.ocado.pandateam.newrelic.sync.configuration.ConditionConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.condition.Condition;
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
class ConditionSynchronizer {
    private final NewRelicApi api;
    private final ConditionConfiguration config;

    ConditionSynchronizer(@NonNull NewRelicApi api, @NonNull ConditionConfiguration config) {
        this.api = api;
        this.config = config;
    }

    void sync() {
        LOG.info(format("Synchronizing alerts conditions for policy %s...", config.getPolicyName()));

        Optional<AlertsPolicy> policyOptional = api.getAlertsPoliciesApi().getByName(config.getPolicyName());
        AlertsPolicy policy = policyOptional.orElseThrow(
            () -> new NewRelicSyncException(format("Policy %s does not exist", config.getPolicyName())));


        List<AlertsCondition> allAlertsConditions = api.getAlertsConditionsApi().list(policy.getId());
        List<Integer> updatedAlertsConditionsIds = createOrUpdateAlertsConditions(policy, allAlertsConditions);

        cleanupOldAlertsConditions(policy, allAlertsConditions, updatedAlertsConditionsIds);
        LOG.info(format("Alerts conditions for policy %s synchronized!", config.getPolicyName()));
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
                    .filter(alertCondition -> sameInstance(alertCondition, alertConditionFromConfig))
                    .findFirst();
                if (alertsConditionToUpdate.isPresent()) {
                    AlertsCondition updatedCondition = api.getAlertsConditionsApi().update(
                        alertsConditionToUpdate.get().getId(), alertConditionFromConfig);
                    LOG.info(format("Alerts condition %s (id: %d) updated for policy %s (id: %d)",
                        updatedCondition.getName(), updatedCondition.getId(), policy.getName(), policy.getId()));
                    updatedAlertConditions.add(updatedCondition);
                } else {
                    AlertsCondition newCondition = api.getAlertsConditionsApi().create(
                        policy.getId(), alertConditionFromConfig);
                    LOG.info(format("Alerts condition %s (id: %d) created for policy %s (id: %d)",
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
                    LOG.info(format("Alerts condition %s (id: %d) removed from policy %s (id: %d)",
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
            .terms(TermsUtils.createTerms(condition.getTerms()))
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
            case APM_KT:
                return condition.getEntities().stream()
                    .map(
                        entity -> {
                            Optional<KeyTransaction> ktOptional = api.getKeyTransactionsApi().getByName(entity);
                            KeyTransaction kt = ktOptional.orElseThrow(
                                () -> new NewRelicSyncException(format("Key transaction %s does not exist", entity)));
                            return kt.getId();
                        }
                    )
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
