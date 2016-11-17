package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.sync.configuration.ApplicationConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.ChannelConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.ConditionConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.PolicyConfiguration;
import lombok.NonNull;

import java.util.Collection;

public class Synchronizer {
    private final String apiKey;

    private Collection<ApplicationConfiguration> applicationConfigurations;
    private Collection<PolicyConfiguration> policyConfigurations;
    private Collection<ConditionConfiguration> conditionConfigurations;
    private Collection<ChannelConfiguration> channelConfigurations;

    public Synchronizer(@NonNull String apiKey) {
        this.apiKey = apiKey;
    }

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
        if (conditionConfigurations != null) {
            conditionConfigurations.stream().forEach(
                configuration -> {
                    ConditionSynchronizer synchronizer = new ConditionSynchronizer(api, configuration);
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

    public void setApplicationConfigurations(Collection<ApplicationConfiguration> applicationConfigurations) {
        this.applicationConfigurations = applicationConfigurations;
    }

    public void setPolicyConfigurations(Collection<PolicyConfiguration> policyConfigurations) {
        this.policyConfigurations = policyConfigurations;
    }

    public void setConditionsConfigurations(Collection<ConditionConfiguration> conditionConfigurations) {
        this.conditionConfigurations = conditionConfigurations;
    }

    public void setChannelConfigurations(Collection<ChannelConfiguration> channelConfigurations) {
        this.channelConfigurations = channelConfigurations;
    }

}
