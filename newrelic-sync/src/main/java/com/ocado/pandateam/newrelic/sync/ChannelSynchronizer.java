package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannel;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicyChannels;
import com.ocado.pandateam.newrelic.sync.configuration.ChannelConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.channel.ChannelUtils;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;
import lombok.NonNull;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

        Set<Integer> policyChannelsToCleanup = createOrUpdatePolicyAlertsChannels(policy);
        cleanupPolicyAlertsChannels(policy, policyChannelsToCleanup);

        LOG.info(format("Channels for policy %s synchronized!", config.getPolicyName()));
    }

    private Set<Integer> createOrUpdatePolicyAlertsChannels(AlertsPolicy policy) {
        List<AlertsChannel> allAlertsChannels = api.getAlertsChannelsApi().list();

        Set<Integer> policyChannelsToCleanup = allAlertsChannels.stream()
            .filter(alertsChannel -> alertsChannel.getLinks().getPolicyIds().contains(policy.getId()))
            .map(AlertsChannel::getId)
            .collect(Collectors.toSet());

        Set<Integer> policyChannelsToUpdate = new LinkedHashSet<>();

        config.getChannels().stream().forEach(
            channel -> {
                AlertsChannel mapped = AlertsChannel.builder()
                    .name(channel.getChannelName())
                    .type(channel.getTypeString())
                    .configuration(ChannelUtils.generateAlertsChannelConfiguration(channel))
                    .build();
                List<AlertsChannel> sameInstanceChannels = allAlertsChannels.stream()
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

                policyChannelsToCleanup.addAll(sameInstanceChannels.stream()
                    .map(AlertsChannel::getId)
                    .collect(Collectors.toList()));
                policyChannelsToUpdate.add(updatedChannel.getId());
                policyChannelsToCleanup.remove(updatedChannel.getId());
            }
        );

        api.getAlertsPoliciesApi().updateChannels(
            AlertsPolicyChannels.builder()
                .policyId(policy.getId())
                .channelIds(policyChannelsToUpdate)
                .build()
        );
        return policyChannelsToCleanup;
    }

    private void cleanupPolicyAlertsChannels(AlertsPolicy policy, Set<Integer> policyChannelsToCleanup) {
        policyChannelsToCleanup.stream().forEach(
            policyChannelId -> {
                AlertsChannel removed = api.getAlertsChannelsApi().deleteFromPolicy(policy.getId(), policyChannelId);
                LOG.info(format("Channel %s (id: %d) removed from policy %s (id: %d)!",
                    removed.getName(), removed.getId(), policy.getName(), policy.getId()));

                List<Integer> currentChannelPolicyIds = removed.getLinks().getPolicyIds();
                currentChannelPolicyIds.remove(policy.getId());
                if (currentChannelPolicyIds.isEmpty()) {
                    api.getAlertsChannelsApi().delete(removed.getId());
                    LOG.info(format("Channel %s (id: %d) removed!", removed.getName(), removed.getId()));
                }
            }
        );
    }

}
