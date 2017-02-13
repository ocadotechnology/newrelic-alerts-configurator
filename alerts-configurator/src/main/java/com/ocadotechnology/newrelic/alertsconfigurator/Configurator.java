package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.ApplicationConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.internal.entities.EntityResolver;
import com.ocadotechnology.newrelic.api.NewRelicApi;
import lombok.NonNull;

import java.util.Collection;
import java.util.Collections;

public class Configurator {
    private final ApplicationConfigurator applicationConfigurator;
    private final PolicyConfigurator policyConfigurator;
    private final ConditionConfigurator conditionConfigurator;
    private final ExternalServiceConditionConfigurator externalServiceConditionConfigurator;
    private final ChannelConfigurator channelConfigurator;


    private Collection<ApplicationConfiguration> applicationConfigurations = Collections.emptyList();
    private Collection<PolicyConfiguration> policyConfigurations = Collections.emptyList();

    /**
     * NewRelic Alerts configurator constructor
     * @param apiKey API Key for given NewRelic account
     */
    public Configurator(@NonNull String apiKey) {
        NewRelicApi api = new NewRelicApi(apiKey);
        applicationConfigurator = new ApplicationConfigurator(api);
        policyConfigurator = new PolicyConfigurator(api);
        conditionConfigurator = new ConditionConfigurator(api, EntityResolver.defaultInstance());
        externalServiceConditionConfigurator = new ExternalServiceConditionConfigurator(api, EntityResolver.defaultInstance());
        channelConfigurator = new ChannelConfigurator(api);
    }

    Configurator(ApplicationConfigurator applicationConfigurator,
                 PolicyConfigurator policyConfigurator,
                 ConditionConfigurator conditionConfigurator,
                 ExternalServiceConditionConfigurator externalServiceConditionConfigurator,
                 ChannelConfigurator channelConfigurator) {
        this.applicationConfigurator = applicationConfigurator;
        this.policyConfigurator = policyConfigurator;
        this.conditionConfigurator = conditionConfigurator;
        this.externalServiceConditionConfigurator = externalServiceConditionConfigurator;
        this.channelConfigurator = channelConfigurator;
    }

    /**
     * Synchronizes configurations (see {@link Configurator#setApplicationConfigurations(Collection)}
     * and {@link Configurator#setPolicyConfigurations(Collection)}
     */
    public void sync() {
        for (ApplicationConfiguration applicationConfiguration : applicationConfigurations) {
            applicationConfigurator.sync(applicationConfiguration);
        }

        for (PolicyConfiguration configuration : policyConfigurations) {
            policyConfigurator.sync(configuration);
            conditionConfigurator.sync(configuration);
            externalServiceConditionConfigurator.sync(configuration);
            channelConfigurator.sync(configuration);
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
