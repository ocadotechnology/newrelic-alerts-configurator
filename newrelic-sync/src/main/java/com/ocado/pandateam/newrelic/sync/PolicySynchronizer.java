package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.applications.Application;
import com.ocado.pandateam.newrelic.api.model.conditions.Terms;
import com.ocado.pandateam.newrelic.api.model.conditions.external.ExternalServiceCondition;
import com.ocado.pandateam.newrelic.api.model.policies.AlertPolicy;
import com.ocado.pandateam.newrelic.sync.configuration.PolicyConfiguration;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;

import java.util.Optional;

import static java.lang.String.format;

public class PolicySynchronizer {

    private final NewRelicApi api;

    private final PolicyConfiguration config;

    public PolicySynchronizer(NewRelicApi api, PolicyConfiguration config) {
        this.api = api;
        this.config = config;
    }

    public void sync() throws NewRelicApiException, NewRelicSyncException {
        Optional<Application> applicationOptional = api.getApplicationsApi().getByName(config.getApplicationName());
        Application application = applicationOptional.orElseThrow(
                () -> new NewRelicSyncException(format("Application %s does not exist", config.getApplicationName())));

        Optional<AlertPolicy> policyOptional = api.getAlertsPoliciesApi().getByName(config.getPolicyName());
        AlertPolicy policy = policyOptional.orElseGet(
                () -> {
                    try {
                        return api.getAlertsPoliciesApi().create(config.getPolicyName());
                    } catch (NewRelicApiException e) {
                        return null;
                    }
                }
        );

        if (api.getAlertsExternalServiceConditionsApi().list(policy.getId()).getList().isEmpty()) {
            api.getAlertsExternalServiceConditionsApi().create(policy.getId(),
                    ExternalServiceCondition.builder()
                            .enabled(true)
                            .name("KMS average response time")
                            .type("apm_external_service")
                            .metric("response_time_average")
                            .externalServiceUrl("kms.eu-west-1.amazonaws.com")
                            .entities(new Integer[]{application.getId()})
                            .terms(new Terms[]{Terms.builder()
                                    .duration("5")
                                    .operator("above")
                                    .threshold("5")
                                    .timeFunction("all")
                                    .priority("critical")
                                    .build()}).build()
            );
        }
    }
}
