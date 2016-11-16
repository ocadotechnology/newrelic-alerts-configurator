package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.channels.AlertChannel;
import com.ocado.pandateam.newrelic.api.model.policies.AlertPolicy;
import com.ocado.pandateam.newrelic.api.model.policies.AlertPolicyChannels;
import com.ocado.pandateam.newrelic.sync.configuration.channel.ChannelUtils;
import com.ocado.pandateam.newrelic.sync.configuration.ChannelConfiguration;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class ChannelSynchronizer {

    private final NewRelicApi api;

    private final ChannelConfiguration config;

    public ChannelSynchronizer(NewRelicApi api, ChannelConfiguration config) {
        this.api = api;
        this.config = config;
    }

    public void sync() throws NewRelicApiException, NewRelicSyncException {
        Optional<AlertPolicy> policyOptional = api.getAlertsPoliciesApi().getByName(config.getPolicyName());
        AlertPolicy policy = policyOptional.orElseThrow(
                () -> new NewRelicSyncException(format("Policy %s does not exist", config.getPolicyName())));

        List<Integer> policyChannels = updateChannels();
        updateAlertPolicyChannels(policy, policyChannels);
    }

    private List<Integer> updateChannels() throws NewRelicApiException, NewRelicSyncException {
        List<AlertChannel> alertChannels = api.getAlertsChannelsApi().list();
        List<Integer> policyChannels = new LinkedList<>();

        config.getChannels().stream().forEach(
                channel -> {
                    AlertChannel mapped = AlertChannel.builder()
                            .name(channel.getChannelName())
                            .type(channel.getType())
                            .configuration(channel.getAlertChannelConfiguration())
                            .build();
                    List<AlertChannel> sameInstanceChannels = alertChannels.stream()
                            .filter(alertChannel -> ChannelUtils.sameInstance(mapped, alertChannel))
                            .collect(Collectors.toList());

                    AlertChannel updatedChannel = sameInstanceChannels.stream()
                            .filter(alertChannel -> ChannelUtils.same(mapped, alertChannel))
                            .findFirst()
                            .orElseGet(() -> api.getAlertsChannelsApi().create(mapped));

                    int id = updatedChannel.getId();
                    policyChannels.add(id);
                    sameInstanceChannels.stream().map(AlertChannel::getId).filter(channelId -> channelId != id).forEach(
                            channelId -> api.getAlertsChannelsApi().delete(channelId)
                    );
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
