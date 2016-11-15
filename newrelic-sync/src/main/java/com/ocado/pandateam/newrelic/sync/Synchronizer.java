package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.applications.Application;
import com.ocado.pandateam.newrelic.api.model.applications.Settings;
import com.ocado.pandateam.newrelic.api.model.channels.AlertChannel;
import com.ocado.pandateam.newrelic.api.model.conditions.Terms;
import com.ocado.pandateam.newrelic.api.model.conditions.external.ExternalServiceCondition;
import com.ocado.pandateam.newrelic.api.model.policies.AlertPolicy;
import com.ocado.pandateam.newrelic.api.model.policies.AlertPolicyChannels;
import com.ocado.pandateam.newrelic.sync.channel.ChannelUtils;
import com.ocado.pandateam.newrelic.sync.configuration.ChannelConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Synchronizer {

    private final NewRelicApi api;

    private final Configuration config;

    private final ChannelConfiguration channelConfig;

    public Synchronizer(NewRelicApi api, Configuration config, ChannelConfiguration channelConfig) {
        this.api = api;
        this.config = config;
        this.channelConfig = channelConfig;
    }

    public void sync() throws NewRelicApiException, NewRelicSyncException {
        List<Integer> policyChannels = updateChannels();

        Optional<Application> applicationOptional = api.getApplicationsApi().getByName(config.getApplicationName());

        Application application = applicationOptional.orElseThrow(NewRelicSyncException::new);

        Settings settings = Settings.builder()
                .appApdexThreshold(config.getAppApdexThreshold())
                .endUserApdexThreshold(config.getUserApdexThreshold())
                .build();
        Application applicationUpdate = Application.builder()
                .name(config.getApplicationName())
                .settings(settings)
                .build();
        application = api.getApplicationsApi().update(application.getId(), applicationUpdate);

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

        updateAlertPolicyChannels(policy, policyChannels);

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

    private List<Integer> updateChannels() throws NewRelicApiException, NewRelicSyncException {
        List<AlertChannel> alertChannels = api.getAlertsChannelsApi().list();
        List<Integer> policyChannels = new LinkedList<>();

        channelConfig.getChannels().stream().forEach(
                channel -> {
                    AlertChannel mapped = channel.getAsAlertChannel();
                    List<AlertChannel> sameInstanceChannels = alertChannels.stream()
                            .filter(alertChannel -> ChannelUtils.sameInstance(mapped, alertChannel))
                            .collect(Collectors.toList());

                    AlertChannel updatedChannel = sameInstanceChannels.stream()
                            .filter(alertChannel -> ChannelUtils.same(mapped, alertChannel))
                            .findFirst()
                            .orElse(api.getAlertsChannelsApi().create(mapped));

                    int id = updatedChannel.getId();
                    sameInstanceChannels.stream().map(AlertChannel::getId).filter(channelId -> channelId != id).forEach(
                            channelId -> api.getAlertsChannelsApi().delete(channelId)
                    );
                    policyChannels.add(updatedChannel.getId());
                }
        );

        return policyChannels;
    }

    private void updateAlertPolicyChannels(AlertPolicy policy, List<Integer> policyChannels) throws NewRelicApiException {
        api.getAlertsPoliciesApi().updateChannels(
                AlertPolicyChannels.builder()
                        .policyId(policy.getId())
                        .channelIds(policyChannels)
                        .build()
        );
    }
}
