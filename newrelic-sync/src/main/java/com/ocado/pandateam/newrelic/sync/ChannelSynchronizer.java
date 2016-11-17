package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannel;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicyChannels;
import com.ocado.pandateam.newrelic.sync.configuration.ChannelConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.channel.ChannelUtils;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

class ChannelSynchronizer {

    private final NewRelicApi api;
    private final ChannelConfiguration config;

    ChannelSynchronizer(@NonNull NewRelicApi api, @NonNull ChannelConfiguration config) {
        this.api = api;
        this.config = config;
    }

    void sync() throws NewRelicApiException, NewRelicSyncException {
        Optional<AlertsPolicy> policyOptional = api.getAlertsPoliciesApi().getByName(config.getPolicyName());
        AlertsPolicy policy = policyOptional.orElseThrow(
            () -> new NewRelicSyncException(format("Policy %s does not exist", config.getPolicyName())));

        List<AlertsChannel> allAlertChannels = api.getAlertsChannelsApi().list();
        List<Integer> policyChannels = updateChannels(allAlertChannels);

        List<Integer> oldPolicyChannelsToCleanup = getOldPolicyChannels(policy.getId(), allAlertChannels);
        oldPolicyChannelsToCleanup.removeAll(policyChannels);
        cleanupAlertPolicyChannels(policy.getId(), oldPolicyChannelsToCleanup);

        addAlertPolicyChannels(policy.getId(), policyChannels);
    }

    private List<Integer> updateChannels(List<AlertsChannel> alertChannels) throws NewRelicApiException, NewRelicSyncException {
        List<Integer> policyChannels = new LinkedList<>();

        config.getChannels().stream().forEach(
            channel -> {
                AlertsChannel mapped = AlertsChannel.builder()
                    .name(channel.getChannelName())
                    .type(channel.getType())
                    .configuration(channel.getAlertChannelConfiguration())
                    .build();
                List<AlertsChannel> sameInstanceChannels = alertChannels.stream()
                    .filter(alertChannel -> ChannelUtils.sameInstance(mapped, alertChannel))
                    .collect(Collectors.toList());

                AlertsChannel updatedChannel = sameInstanceChannels.stream()
                    .filter(alertChannel -> ChannelUtils.same(mapped, alertChannel))
                    .findFirst()
                    .orElseGet(() -> api.getAlertsChannelsApi().create(mapped));

                int id = updatedChannel.getId();
                policyChannels.add(id);
                sameInstanceChannels.stream().map(AlertsChannel::getId).filter(channelId -> channelId != id).forEach(
                    channelId -> api.getAlertsChannelsApi().delete(channelId)
                );
            }
        );

        return policyChannels;
    }

    private void addAlertPolicyChannels(Integer policyId, List<Integer> policyChannels) throws NewRelicApiException {
        api.getAlertsPoliciesApi().updateChannels(
            AlertsPolicyChannels.builder()
                .policyId(policyId)
                .channelIds(policyChannels)
                .build()
        );
    }

    private List<Integer> getOldPolicyChannels(Integer policyId, List<AlertsChannel> alertChannels) {
        return alertChannels.stream()
            .filter(policyChannel -> policyChannel.getLinks().getPolicyIds().contains(policyId))
            .map(AlertsChannel::getId)
            .collect(Collectors.toList());
    }

    private void cleanupAlertPolicyChannels(Integer policyId, List<Integer> oldPolicyChannels) {
        oldPolicyChannels.stream().forEach(
            channelId -> {
                AlertsChannel alertsChannel = api.getAlertsChannelsApi().deleteFromPolicy(policyId, channelId);
                List<Integer> currentChannelPolicyIds = alertsChannel.getLinks().getPolicyIds();
                currentChannelPolicyIds.remove(policyId);
                if (currentChannelPolicyIds.isEmpty()) {
                    api.getAlertsChannelsApi().delete(channelId);
                }
            }
        );
    }
}
