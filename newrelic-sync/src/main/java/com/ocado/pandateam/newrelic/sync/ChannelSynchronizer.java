package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannel;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicyChannels;
import com.ocado.pandateam.newrelic.sync.configuration.PolicyConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.channel.Channel;
import com.ocado.pandateam.newrelic.sync.configuration.channel.ChannelUtils;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
class ChannelSynchronizer {
    private final NewRelicApi api;

    ChannelSynchronizer(@NonNull NewRelicApi api) {
        this.api = api;
    }

    void sync(@NonNull PolicyConfiguration config) {
        LOG.info("Synchronizing alerts channels for policy {}...", config.getPolicyName());

        AlertsPolicy policy = api.getAlertsPoliciesApi().getByName(config.getPolicyName()).orElseThrow(
            () -> new NewRelicSyncException(format("Policy %s does not exist", config.getPolicyName())));

        List<AlertsChannel> alertsChannelsFromConfig = config.getChannels().stream()
            .map(this::createAlertsChannel)
            .collect(Collectors.toList());
        Set<Integer> policyChannelsToCleanup = createOrUpdatePolicyAlertsChannels(policy, alertsChannelsFromConfig);
        cleanupPolicyAlertsChannels(policy, policyChannelsToCleanup);

        LOG.info("Alerts channels for policy {} synchronized", config.getPolicyName());
    }

    private Set<Integer> createOrUpdatePolicyAlertsChannels(AlertsPolicy policy, List<AlertsChannel> alertsChannelsFromConfig) {
        List<AlertsChannel> allAlertsChannels = api.getAlertsChannelsApi().list();

        Set<Integer> policyChannelsToCleanup = allAlertsChannels.stream()
            .filter(alertsChannel -> alertsChannel.getLinks().getPolicyIds().contains(policy.getId()))
            .map(AlertsChannel::getId)
            .collect(Collectors.toSet());

        Set<Integer> policyChannelsToUpdate = new LinkedHashSet<>();


        for (AlertsChannel alertsChannelFromConfig : alertsChannelsFromConfig) {
            List<AlertsChannel> sameInstanceChannels = allAlertsChannels.stream()
                .filter(alertChannel -> ChannelUtils.sameInstance(alertsChannelFromConfig, alertChannel))
                .collect(Collectors.toList());

            AlertsChannel updatedChannel = sameInstanceChannels.stream()
                .filter(alertChannel -> ChannelUtils.same(alertsChannelFromConfig, alertChannel))
                .findFirst()
                .orElseGet(() -> {
                    AlertsChannel newChannel = api.getAlertsChannelsApi().create(alertsChannelFromConfig);
                    LOG.info("Alerts channel {} (id: {}) created", newChannel.getName(), newChannel.getId());
                    return newChannel;
                });

            policyChannelsToCleanup.addAll(sameInstanceChannels.stream()
                .map(AlertsChannel::getId)
                .collect(Collectors.toList()));
            policyChannelsToUpdate.add(updatedChannel.getId());
            policyChannelsToCleanup.remove(updatedChannel.getId());
        }


        api.getAlertsPoliciesApi().updateChannels(
            AlertsPolicyChannels.builder()
                .policyId(policy.getId())
                .channelIds(policyChannelsToUpdate)
                .build()
        );
        return policyChannelsToCleanup;
    }

    private AlertsChannel createAlertsChannel(Channel channel) {
        return AlertsChannel.builder()
            .name(channel.getChannelName())
            .type(channel.getTypeString())
            .configuration(ChannelUtils.generateAlertsChannelConfiguration(channel))
            .build();
    }

    private void cleanupPolicyAlertsChannels(AlertsPolicy policy, Set<Integer> policyChannelsToCleanup) {
        for (Integer policyChannelId : policyChannelsToCleanup) {
            AlertsChannel removed = api.getAlertsChannelsApi().deleteFromPolicy(policy.getId(), policyChannelId);
            LOG.info("Alerts channel {} (id: {}) removed from policy {} (id: {})",
                removed.getName(), removed.getId(), policy.getName(), policy.getId());

            List<Integer> currentChannelPolicyIds = removed.getLinks().getPolicyIds();
            currentChannelPolicyIds.remove(policy.getId());
            if (currentChannelPolicyIds.isEmpty()) {
                api.getAlertsChannelsApi().delete(removed.getId());
                LOG.info("Alerts channel {} (id: {}) removed", removed.getName(), removed.getId());
            }
        }
    }

}
