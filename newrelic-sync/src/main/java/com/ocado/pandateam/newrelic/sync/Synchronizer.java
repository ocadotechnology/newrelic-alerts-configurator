package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.AlertChannel;
import com.ocado.pandateam.newrelic.api.model.AlertPolicy;
import com.ocado.pandateam.newrelic.api.model.AlertPolicyChannels;
import com.ocado.pandateam.newrelic.api.model.Application;
import com.ocado.pandateam.newrelic.api.model.ExternalServiceCondition;
import com.ocado.pandateam.newrelic.api.model.Settings;
import com.ocado.pandateam.newrelic.api.model.Terms;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Synchronizer {

    private final NewRelicApi api;

    private final Configuration config;

    public Synchronizer(NewRelicApi api, Configuration config) {
        this.api = api;
        this.config = config;
    }

    public void sync() throws NewRelicApiException, NewRelicSyncException {
        Optional<Application> applicationOptional = api.getApplicationByName(config.getApplicationName());

        Application application = applicationOptional.orElseThrow(NewRelicSyncException::new);

        Settings settings = Settings.builder()
                .appApdexThreshold(config.getAppApdexThreshold())
                .endUserApdexThreshold(config.getUserApdexThreshold())
                .build();
        Application applicationUpdate = Application.builder()
                .name(config.getApplicationName())
                .settings(settings)
                .build();
        application = api.updateApplication(application.getId(), applicationUpdate);

        Optional<AlertPolicy> policyOptional = api.getAlertPolicyByName(config.getPolicyName());
        AlertPolicy policy = policyOptional.orElseGet(
                () -> {
                    try {
                        return api.createAlertPolicy(config.getPolicyName());
                    } catch (NewRelicApiException e) {
                        return null;
                    }
                }
        );


        List<AlertChannel> alertChannels = api.listAlertChannels();

        List<String> currentEmailChannels = alertChannels.stream()
                .filter(alertChannel -> alertChannel.getType().equals("email"))
                .map(AlertChannel::getName)
                .collect(Collectors.toList());

        List<Integer> policyChannels = new LinkedList<>();

        config.getEmailChannels().stream().forEach(
                emailChannel -> {
                    if (currentEmailChannels.contains(emailChannel.getChannelName())) {
                        // if not equal delete create
                        // add id
                    } else {
                        try {
                            AlertChannel newChannel = api.createEmailAlertChannel(
                                    emailChannel.getChannelName(),
                                    emailChannel.getEmailAddress(),
                                    emailChannel.getIncludeJsonAttachment()).get();
                            policyChannels.add(newChannel.getId());
                        } catch (NewRelicApiException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        api.updateAlertsPolicyChannels(
                AlertPolicyChannels.builder()
                        .policyId(policy.getId())
                        .channelIds(policyChannels)
                        .build()
        );

        if (api.listExternalServiceConditions(policy.getId()).getList().isEmpty()) {
            api.createExternalServiceCondition(policy.getId(),
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
