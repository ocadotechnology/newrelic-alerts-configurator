package com.ocado.panda.newrelic.sync;

import com.ocado.panda.newrelic.api.NewRelicApi;
import com.ocado.panda.newrelic.sync.configuration.ApplicationConfiguration;
import com.ocado.panda.newrelic.sync.configuration.PolicyConfiguration;
import lombok.NonNull;

import java.util.Collection;
import java.util.Collections;

public class Synchronizer {
    private final ApplicationSynchronizer applicationSynchronizer;
    private final PolicySynchronizer policySynchronizer;
    private final ConditionSynchronizer conditionSynchronizer;
    private final ExternalServiceConditionSynchronizer externalServiceConditionSynchronizer;
    private final ChannelSynchronizer channelSynchronizer;


    private Collection<ApplicationConfiguration> applicationConfigurations = Collections.emptyList();
    private Collection<PolicyConfiguration> policyConfigurations = Collections.emptyList();

    /**
     * NewRelic Alerts synchronizer constructor
     * @param apiKey API Key for given NewRelic account
     */
    public Synchronizer(@NonNull String apiKey) {
        NewRelicApi api = new NewRelicApi(apiKey);
        applicationSynchronizer = new ApplicationSynchronizer(api);
        policySynchronizer = new PolicySynchronizer(api);
        conditionSynchronizer = new ConditionSynchronizer(api);
        externalServiceConditionSynchronizer = new ExternalServiceConditionSynchronizer(api);
        channelSynchronizer = new ChannelSynchronizer(api);
    }

    Synchronizer(ApplicationSynchronizer applicationSynchronizer,
                 PolicySynchronizer policySynchronizer,
                 ConditionSynchronizer conditionSynchronizer,
                 ExternalServiceConditionSynchronizer externalServiceConditionSynchronizer,
                 ChannelSynchronizer channelSynchronizer) {
        this.applicationSynchronizer = applicationSynchronizer;
        this.policySynchronizer = policySynchronizer;
        this.conditionSynchronizer = conditionSynchronizer;
        this.externalServiceConditionSynchronizer = externalServiceConditionSynchronizer;
        this.channelSynchronizer = channelSynchronizer;
    }

    /**
     * Synchronizes configurations (see {@link Synchronizer#setApplicationConfigurations(Collection)}
     * and {@link Synchronizer#setPolicyConfigurations(Collection)}
     */
    public void sync() {
        for (ApplicationConfiguration applicationConfiguration : applicationConfigurations) {
            applicationSynchronizer.sync(applicationConfiguration);
        }

        for (PolicyConfiguration configuration : policyConfigurations) {
            policySynchronizer.sync(configuration);
            conditionSynchronizer.sync(configuration);
            externalServiceConditionSynchronizer.sync(configuration);
            channelSynchronizer.sync(configuration);
        }
    }

    /**
     * @param applicationConfigurations collection of application configurations to synchronize
     * @see ApplicationConfiguration
     */
    public void setApplicationConfigurations(@NonNull Collection<ApplicationConfiguration> applicationConfigurations) {
        this.applicationConfigurations = applicationConfigurations;
    }

    /**
     * @param policyConfigurations collection of policy configurations to synchronize
     * @see PolicyConfiguration
     */
    public void setPolicyConfigurations(@NonNull Collection<PolicyConfiguration> policyConfigurations) {
        this.policyConfigurations = policyConfigurations;
    }
}
