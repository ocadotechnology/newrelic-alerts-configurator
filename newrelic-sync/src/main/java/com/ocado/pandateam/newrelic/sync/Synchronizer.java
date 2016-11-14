package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.AlertChannel;
import com.ocado.pandateam.newrelic.api.model.AlertPolicy;
import com.ocado.pandateam.newrelic.api.model.AlertPolicyChannels;
import com.ocado.pandateam.newrelic.api.model.Application;
import com.ocado.pandateam.newrelic.api.model.Settings;

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
        api.updateApplication(application.getId(), applicationUpdate);

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

        List<String> currentEmails = alertChannels.stream()
                .filter(alertChannel -> alertChannel.getType().equals("email"))
                .map(alertChannel -> alertChannel.getConfiguration().getRecipients())
                .collect(Collectors.toList());

        List<AlertChannel> emailChannels = new LinkedList<>();

        config.getEmailChannels().stream()
                .filter(s -> !currentEmails.contains(s))
                .forEach(s -> {
                            try {
                                alertChannels.add(api.createEmailAlertChannel(s, s, Boolean.FALSE.toString()).get());
                            } catch (NewRelicApiException e) {
                                e.printStackTrace();
                            }
                        }
                );

        List<Integer> emails = alertChannels.stream().filter(
                alertChannel -> alertChannel.getType().equals("email")
        ).filter(alertChannel -> currentEmails.contains(alertChannel.getConfiguration().getRecipients()))
                .map(AlertChannel::getId).collect(Collectors.toList());

        api.updateAlertsPolicyChannels(
                AlertPolicyChannels.builder()
                        .policyId(policy.getId())
                        .channelIds(emails)
                        .build()
        );
    }
}
