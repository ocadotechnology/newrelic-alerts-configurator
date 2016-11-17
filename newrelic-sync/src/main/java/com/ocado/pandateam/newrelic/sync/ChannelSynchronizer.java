package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
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
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.String.format;

class ChannelSynchronizer {
    private static final Logger LOG = Logger.getLogger(ChannelSynchronizer.class.getName());

    private final NewRelicApi api;
    private final ChannelConfiguration config;

    ChannelSynchronizer(@NonNull NewRelicApi api, @NonNull ChannelConfiguration config) {
        this.api = api;
        this.config = config;
    }

    void sync() {
        LOG.info(format("Synchronizing channels for policy %s...", config.getPolicyName()));

        Optional<AlertsPolicy> policyOptional = api.getAlertsPoliciesApi().getByName(config.getPolicyName());
        AlertsPolicy policy = policyOptional.orElseThrow(
            () -> new NewRelicSyncException(format("Policy %s does not exist", config.getPolicyName())));

        List<AlertsChannel> allAlertsChannels = api.getAlertsChannelsApi().list();
        List<AlertsChannel> currentPolicyAlertsChannels = createOrUpdateAlertsChannels(allAlertsChannels);

        List<AlertsChannel> currentPolicyOldAlertsChannelsToRemove = getOldPolicyAlertsChannelsToRemove(policy, allAlertsChannels,
            currentPolicyAlertsChannels);
        cleanupAlertsPolicyChannels(policy, currentPolicyOldAlertsChannelsToRemove);

        addAlertsPolicyChannels(policy, currentPolicyAlertsChannels);
        LOG.info(format("Channels for policy %s synchronized!", config.getPolicyName()));
    }

    private List<AlertsChannel> createOrUpdateAlertsChannels(List<AlertsChannel> alertChannels) {
        List<AlertsChannel> policyChannels = new LinkedList<>();

        config.getChannels().stream().forEach(
            channel -> {
                AlertsChannel mapped = AlertsChannel.builder()
                    .name(channel.getChannelName())
                    .type(channel.getTypeString())
                    .configuration(ChannelUtils.generateAlertsChannelConfiguration(channel))
                    .build();
                List<AlertsChannel> sameInstanceChannels = alertChannels.stream()
                    .filter(alertChannel -> ChannelUtils.sameInstance(mapped, alertChannel))
                    .collect(Collectors.toList());

                AlertsChannel updatedChannel = sameInstanceChannels.stream()
                    .filter(alertChannel -> ChannelUtils.same(mapped, alertChannel))
                    .findFirst()
                    .orElseGet(() -> {
                        AlertsChannel newChannel = api.getAlertsChannelsApi().create(mapped);
                        LOG.info(format("Channel %s (id: %d) created!", newChannel.getName(), newChannel.getId()));
                        return newChannel;
                    });

                policyChannels.add(updatedChannel);
                sameInstanceChannels.stream()
                    .filter(sameInstanceChannel -> Integer.compare(sameInstanceChannel.getId(), updatedChannel.getId()) != 0)
                    .forEach(sameInstanceChannel -> {
                        api.getAlertsChannelsApi().delete(sameInstanceChannel.getId());
                        LOG.info(format("Channel %s (id: %d) removed!",
                            sameInstanceChannel.getName(), sameInstanceChannel.getId()));
                    });
            }
        );

        return policyChannels;
    }

    private void addAlertsPolicyChannels(AlertsPolicy policy, List<AlertsChannel> policyChannels) {
        List<Integer> policyChannelsIds = policyChannels.stream().map(AlertsChannel::getId).collect(Collectors.toList());

        api.getAlertsPoliciesApi().updateChannels(
            AlertsPolicyChannels.builder()
                .policyId(policy.getId())
                .channelIds(policyChannelsIds)
                .build()
        );
    }

    private List<AlertsChannel> getOldPolicyAlertsChannelsToRemove(AlertsPolicy policy, List<AlertsChannel> alertChannels,
                                                                   List<AlertsChannel> currentPolicyChannels) {
        List<Integer> currentPolicyChannelsIds = currentPolicyChannels.stream()
            .map(AlertsChannel::getId)
            .collect(Collectors.toList());

        return alertChannels.stream()
            .filter(policyChannel -> policyChannel.getLinks().getPolicyIds().contains(policy.getId()))
            .filter(policyChannel -> !currentPolicyChannelsIds.contains(policyChannel.getId()))
            .collect(Collectors.toList());
    }

    private void cleanupAlertsPolicyChannels(AlertsPolicy policy, List<AlertsChannel> oldPolicyChannels) {
        oldPolicyChannels.stream().forEach(
            oldChannel -> {
                api.getAlertsChannelsApi().deleteFromPolicy(policy.getId(), oldChannel.getId());
                LOG.info(format("Channel %s (id: %d) removed from policy %s (id: %d)!",
                    oldChannel.getName(), oldChannel.getId(), policy.getName(), policy.getId()));

                List<Integer> currentChannelPolicyIds = oldChannel.getLinks().getPolicyIds();
                currentChannelPolicyIds.remove(policy.getId());
                if (currentChannelPolicyIds.isEmpty()) {
                    api.getAlertsChannelsApi().delete(oldChannel.getId());
                    LOG.info(format("Channel %s (id: %d) removed!", oldChannel.getName(), oldChannel.getId()));
                }
            }
        );
    }
}
