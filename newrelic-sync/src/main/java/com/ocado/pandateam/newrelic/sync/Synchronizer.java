package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.sync.configuration.ApplicationConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.ChannelConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.ConditionsConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.PolicyConfiguration;
import lombok.Builder;
import lombok.NonNull;

import java.util.Collection;

@Builder
public class Synchronizer {
    @NonNull
    private String apiKey;

    private Collection<ApplicationConfiguration> applicationConfigurations;
    private Collection<PolicyConfiguration> policyConfigurations;
    private Collection<ConditionsConfiguration> conditionsConfigurations;
    private Collection<ChannelConfiguration> channelConfigurations;

    public void sync() {
        NewRelicApi api = new NewRelicApi(apiKey);
        if (applicationConfigurations != null) {
            applicationConfigurations.stream().forEach(
                configuration -> {
                    ApplicationSynchronizer synchronizer = new ApplicationSynchronizer(api, configuration);
                    synchronizer.sync();
                });
        }
        if (policyConfigurations != null) {
            policyConfigurations.stream().forEach(
                configuration -> {
                    PolicySynchronizer synchronizer = new PolicySynchronizer(api, configuration);
                    synchronizer.sync();
                });
        }
        if (conditionsConfigurations != null) {
            conditionsConfigurations.stream().forEach(
                configuration -> {
                    ConditionsSynchronizer synchronizer = new ConditionsSynchronizer(api, configuration);
                    synchronizer.sync();
                });
        }
        if (channelConfigurations != null) {
            channelConfigurations.stream().forEach(
                configuration -> {
                    ChannelSynchronizer synchronizer = new ChannelSynchronizer(api, configuration);
                    synchronizer.sync();
                });
        }
    }
}
