package com.ocado.panda.newrelic.sync;

import com.ocado.panda.newrelic.api.NewRelicApi;
import com.ocado.panda.newrelic.api.model.channels.AlertsChannel;
import com.ocado.panda.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.panda.newrelic.api.model.policies.AlertsPolicyChannels;
import com.ocado.panda.newrelic.sync.configuration.PolicyConfiguration;
import com.ocado.panda.newrelic.sync.configuration.channel.Channel;
import com.ocado.panda.newrelic.sync.exception.NewRelicSyncException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
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

        Set<Integer> policyChannelsToCleanup = createOrUpdatePolicyAlertsChannels(policy, config.getChannels());
        cleanupPolicyAlertsChannels(policy, policyChannelsToCleanup);

        LOG.info("Alerts channels for policy {} synchronized", config.getPolicyName());
    }

    private Set<Integer> createOrUpdatePolicyAlertsChannels(AlertsPolicy policy, Collection<Channel> channelsFromConfig) {
        List<AlertsChannel> allAlertsChannels = api.getAlertsChannelsApi().list();

        Set<Integer> policyChannelsToCleanup = getAllPolicyAlertsChannelsIds(policy, allAlertsChannels);
        Set<Integer> policyChannelsToUpdate = new LinkedHashSet<>();

        for (Channel channelFromConfig : channelsFromConfig) {
            AlertsChannel alertsChannelFromConfig = toAlertsChannel(channelFromConfig);
            Collection<AlertsChannel> sameInstanceAlertsChannels = getSameInstanceAlertsChannels(allAlertsChannels,
                alertsChannelFromConfig);

            AlertsChannel syncedChannel = findSameOrCreate(alertsChannelFromConfig, sameInstanceAlertsChannels);

            policyChannelsToCleanup.addAll(sameInstanceAlertsChannels.stream()
                .map(AlertsChannel::getId)
                .collect(Collectors.toList()));
            policyChannelsToUpdate.add(syncedChannel.getId());
            policyChannelsToCleanup.remove(syncedChannel.getId());
        }


        api.getAlertsPoliciesApi().updateChannels(
            AlertsPolicyChannels.builder()
                .policyId(policy.getId())
                .channelIds(policyChannelsToUpdate)
                .build()
        );
        return policyChannelsToCleanup;
    }

    private Set<Integer> getAllPolicyAlertsChannelsIds(AlertsPolicy policy, List<AlertsChannel> allAlertsChannels) {
        return allAlertsChannels.stream()
            .filter(alertsChannel -> alertsChannel.getLinks().getPolicyIds().contains(policy.getId()))
            .map(AlertsChannel::getId)
            .collect(Collectors.toSet());
    }

    private Collection<AlertsChannel> getSameInstanceAlertsChannels(List<AlertsChannel> allAlertsChannels,
                                                                      AlertsChannel alertsChannelFromConfig) {
        return allAlertsChannels.stream()
            .filter(alertsChannelFromConfig::sameInstance)
            .collect(Collectors.toList());
    }

    private AlertsChannel findSameOrCreate(AlertsChannel alertsChannelFromConfig, Collection<AlertsChannel> sameInstanceChannels) {
        return sameInstanceChannels.stream()
            .filter(alertsChannelFromConfig::same)
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
            .configuration(channel.getChannelTypeSupport().generateAlertsChannelConfiguration())
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

        List<Integer> currentChannelPolicyIds = removed.getLinks().getPolicyIds();
        currentChannelPolicyIds.remove(policy.getId());
        if (currentChannelPolicyIds.isEmpty()) {
            api.getAlertsChannelsApi().delete(removed.getId());
            LOG.info("Alerts channel {} (id: {}) removed", removed.getName(), removed.getId());
        }
    }

}
