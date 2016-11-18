package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.sync.configuration.ApplicationConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.ChannelConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.ConditionConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.ExternalServiceConditionConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.PolicyConfiguration;
import lombok.NonNull;

import java.util.Collection;

public class Synchronizer {
    private final String apiKey;

    private Collection<ApplicationConfiguration> applicationConfigurations;
    private Collection<PolicyConfiguration> policyConfigurations;
    private Collection<ConditionConfiguration> conditionConfigurations;
    private Collection<ExternalServiceConditionConfiguration> externalServiceConditionConfigurations;
    private Collection<ChannelConfiguration> channelConfigurations;

    public Synchronizer(@NonNull String apiKey) {
        this.apiKey = apiKey;
    }

    public void sync() {
        NewRelicApi api = new NewRelicApi(apiKey);
        if (applicationConfigurations != null) {
            for (ApplicationConfiguration configuration : applicationConfigurations) {
                ApplicationSynchronizer synchronizer = new ApplicationSynchronizer(api, configuration);
                synchronizer.sync();
            }
        }
        if (policyConfigurations != null) {
            for (PolicyConfiguration configuration : policyConfigurations) {
                PolicySynchronizer synchronizer = new PolicySynchronizer(api, configuration);
                synchronizer.sync();
            }
        }
        if (conditionConfigurations != null) {
            for (ConditionConfiguration configuration : conditionConfigurations) {
                ConditionSynchronizer synchronizer = new ConditionSynchronizer(api, configuration);
                synchronizer.sync();
            }
        }
        if (externalServiceConditionConfigurations != null) {
            for (ExternalServiceConditionConfiguration configuration : externalServiceConditionConfigurations) {
                ExternalServiceConditionSynchronizer synchronizer = new ExternalServiceConditionSynchronizer(api, configuration);
                synchronizer.sync();
            }
        }
        if (channelConfigurations != null) {
            for (ChannelConfiguration configuration : channelConfigurations) {
                ChannelSynchronizer synchronizer = new ChannelSynchronizer(api, configuration);
                synchronizer.sync();
            }
        }
    }

    public void setApplicationConfigurations(Collection<ApplicationConfiguration> applicationConfigurations) {
        this.applicationConfigurations = applicationConfigurations;
    }

    public void setPolicyConfigurations(Collection<PolicyConfiguration> policyConfigurations) {
        this.policyConfigurations = policyConfigurations;
    }

    public void setChannelConfigurations(Collection<ChannelConfiguration> channelConfigurations) {
        this.channelConfigurations = channelConfigurations;
    }

    public void setExternalServiceConditionConfigurations(Collection<ExternalServiceConditionConfiguration> externalServiceConditionConfigurations) {
        this.externalServiceConditionConfigurations = externalServiceConditionConfigurations;
    }

    public void setConditionConfigurations(Collection<ConditionConfiguration> conditionConfigurations) {
        this.conditionConfigurations = conditionConfigurations;
    }
}
