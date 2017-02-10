package com.ocadotechnology.newrelic.alertconfiguploader;

import com.ocadotechnology.newrelic.alertconfiguploader.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertconfiguploader.configuration.channel.Channel;
import com.ocadotechnology.newrelic.alertconfiguploader.configuration.channel.ChannelType;
import com.ocadotechnology.newrelic.alertconfiguploader.exception.NewRelicSyncException;
import com.ocadotechnology.newrelic.api.NewRelicApi;
import com.ocadotechnology.newrelic.api.model.channels.AlertsChannel;
import com.ocadotechnology.newrelic.api.model.policies.AlertsPolicy;
import com.ocadotechnology.newrelic.api.model.policies.AlertsPolicyChannels;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
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

        Set<Integer> policyChannelsToCleanup = createOrUpdatePolicyAlertsChannels(policy, config.getChannels());
        cleanupPolicyAlertsChannels(policy, policyChannelsToCleanup);

        LOG.info("Alerts channels for policy {} synchronized", config.getPolicyName());
    }

    private Set<Integer> createOrUpdatePolicyAlertsChannels(AlertsPolicy policy, Collection<Channel> channelsFromConfig) {
        List<AlertsChannel> allAlertsChannels = api.getAlertsChannelsApi().list();
        List<AlertsChannel> userChannels = allAlertsChannels.stream()
                .filter(ChannelSynchronizer::isUserChannel)
                .collect(Collectors.toList());
        List<AlertsChannel> nonUserChannels = allAlertsChannels.stream()
                .filter(c -> !isUserChannel(c))
                .collect(Collectors.toList());

        Set<Integer> orphanedChannels = getAllPolicyAlertsChannelsIds(policy, allAlertsChannels);
        Set<Integer> policyChannels = new LinkedHashSet<>();

        for (Channel channelFromConfig : channelsFromConfig) {
            AlertsChannel requestedChannel = toAlertsChannel(channelFromConfig);
            AlertsChannel syncedChannel;
            if (isUserChannel(requestedChannel)) {
                syncedChannel = findUserChannel(requestedChannel, userChannels);
            } else {
                syncedChannel = findSameOrCreate(requestedChannel, nonUserChannels);
            }
            policyChannels.add(syncedChannel.getId());
        }
        orphanedChannels.removeAll(policyChannels);

        api.getAlertsPoliciesApi().updateChannels(
            AlertsPolicyChannels.builder()
                .policyId(policy.getId())
                .channelIds(policyChannels)
                .build()
        );
        return orphanedChannels;
    }

    private AlertsChannel findUserChannel(AlertsChannel requestedChannel, List<AlertsChannel> userChannels) {
        return userChannels.stream()
                .filter(c -> c.getConfiguration().equals(requestedChannel.getConfiguration()))
                .findAny()
                .orElseThrow(() -> new NewRelicSyncException(
                        format("Alerts channel with configuration %s not found", requestedChannel)));
    }

    private Set<Integer> getAllPolicyAlertsChannelsIds(AlertsPolicy policy, List<AlertsChannel> allAlertsChannels) {
        return allAlertsChannels.stream()
            .filter(alertsChannel -> alertsChannel.getLinks().getPolicyIds().contains(policy.getId()))
            .map(AlertsChannel::getId)
            .collect(Collectors.toSet());
    }

    private AlertsChannel findSameOrCreate(AlertsChannel alertsChannelFromConfig, Collection<AlertsChannel> sameInstanceChannels) {
        return sameInstanceChannels.stream()
            .filter(alertsChannelFromConfig::equals)
            .findAny()
            .orElseGet(() -> createAlertsChannel(alertsChannelFromConfig));
    }

    private AlertsChannel createAlertsChannel(AlertsChannel alertsChannelFromConfig) {
        AlertsChannel newChannel = api.getAlertsChannelsApi().create(alertsChannelFromConfig);
        LOG.info("Alerts channel {} (id: {}) created", newChannel.getName(), newChannel.getId());
        return newChannel;
    }

    private AlertsChannel toAlertsChannel(Channel channel) {
        return AlertsChannel.builder()
            .name(channel.getChannelName())
            .type(channel.getTypeString())
            .configuration(channel.getChannelTypeSupport().generateAlertsChannelConfiguration(api))
            .build();
    }

    private void cleanupPolicyAlertsChannels(AlertsPolicy policy, Set<Integer> policyChannelsToCleanup) {
        for (Integer policyChannelId : policyChannelsToCleanup) {
            cleanupPolicyAlertsChannel(policy, policyChannelId);
        }
    }

    private void cleanupPolicyAlertsChannel(AlertsPolicy policy, Integer policyChannelId) {
        AlertsChannel removed = api.getAlertsChannelsApi().deleteFromPolicy(policy.getId(), policyChannelId);
        LOG.info("Alerts channel {} (id: {}) removed from policy {} (id: {})",
            removed.getName(), removed.getId(), policy.getName(), policy.getId());

        if (isUserChannel(removed)) {
            LOG.info("Skipping user channel removal ({})", removed.getName());
        } else {
            List<Integer> currentChannelPolicyIds = new ArrayList<>(removed.getLinks().getPolicyIds());
            currentChannelPolicyIds.remove(policy.getId());
            if (currentChannelPolicyIds.isEmpty()) {
                api.getAlertsChannelsApi().delete(removed.getId());
                LOG.info("Alerts channel {} (id: {}) removed", removed.getName(), removed.getId());
            }
        }
    }

    private static boolean isUserChannel(AlertsChannel channel) {
        String userChannelType = ChannelType.USER.name().toLowerCase();
        return userChannelType.equals(channel.getType());
    }
}
