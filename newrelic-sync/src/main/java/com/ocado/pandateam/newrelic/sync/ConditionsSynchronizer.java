package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.applications.Application;
import com.ocado.pandateam.newrelic.api.model.policies.AlertPolicy;
import com.ocado.pandateam.newrelic.sync.configuration.ConditionConfiguration;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;

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

        Optional<AlertPolicy> policyOptional = api.getAlertsPoliciesApi().getByName(config.getPolicyName());
        AlertPolicy policy = policyOptional.orElseThrow(
                () -> new NewRelicSyncException(format("Policy %s does not exist", config.getPolicyName())));


//        if (api.getAlertsExternalServiceConditionsApi().list(policy.getId()).getList().isEmpty()) {
//            api.getAlertsExternalServiceConditionsApi().create(policy.getId(),
//                    ExternalServiceCondition.builder()
//                            .enabled(true)
//                            .name("KMS average response time")
//                            .type("apm_external_service")
//                            .metric("response_time_average")
//                            .externalServiceUrl("kms.eu-west-1.amazonaws.com")
//                            .entities(new Integer[]{application.getId()})
//                            .terms(new Terms[]{Terms.builder()
//                                    .duration("5")
//                                    .operator("above")
//                                    .threshold("5")
//                                    .timeFunction("all")
//                                    .priority("critical")
//                                    .build()}).build()
//            );
//        }
    }
}
