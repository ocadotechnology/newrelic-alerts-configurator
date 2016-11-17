package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.applications.Application;
import com.ocado.pandateam.newrelic.api.model.conditions.AlertsCondition;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.sync.configuration.ConditionConfiguration;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class ConditionsSynchronizer {

    private final NewRelicApi api;

    private final ConditionConfiguration config;

    public ConditionsSynchronizer(NewRelicApi api, ConditionConfiguration config) {
        this.api = api;
        this.config = config;
    }

    public void sync() throws NewRelicApiException, NewRelicSyncException {
        Optional<Application> applicationOptional = api.getApplicationsApi().getByName(config.getApplicationName());
        Application application = applicationOptional.orElseThrow(
                () -> new NewRelicSyncException(format("Application %s does not exist", config.getApplicationName())));

        Optional<AlertsPolicy> policyOptional = api.getAlertsPoliciesApi().getByName(config.getPolicyName());
        AlertsPolicy policy = policyOptional.orElseThrow(
                () -> new NewRelicSyncException(format("Policy %s does not exist", config.getPolicyName())));


        List<AlertsCondition> alertConditions = api.getAlertsConditionsApi().list(policy.getId());
    }
}
