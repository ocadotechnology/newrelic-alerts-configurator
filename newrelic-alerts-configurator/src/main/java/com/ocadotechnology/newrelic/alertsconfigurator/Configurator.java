package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.ApplicationConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.internal.entities.EntityResolver;
import com.ocadotechnology.newrelic.apiclient.NewRelicApi;
import lombok.NonNull;

import java.util.Collection;
import java.util.Collections;

/**
 * Main class used in configuration process. It is responsible for synchronizing programmatically created definitions of
 * application and policy configurations to NewRelic.
 * <p>
 *
 * Example:
 *
 * <pre>
 * Configurator configurator = new Configurator("NEW_RELIC_REST_API_KEY");
 * configurator.setApplicationConfigurations(Arrays.asList(appConfig1, appConfig2));
 * configurator.setPolicyConfigurations(Arrays.asList(policy1, policy2, policy3));
 * configurator.sync();
 * </pre>
 */
public class Configurator {
    private final ApplicationConfigurator applicationConfigurator;
    private final PolicyConfigurator policyConfigurator;
    private final ConditionConfigurator conditionConfigurator;
    private final ExternalServiceConditionConfigurator externalServiceConditionConfigurator;
    private final NrqlConditionConfigurator nrqlConditionConfigurator;
    private final ChannelConfigurator channelConfigurator;


    private Collection<ApplicationConfiguration> applicationConfigurations = Collections.emptyList();
    private Collection<PolicyConfiguration> policyConfigurations = Collections.emptyList();

    /**
     * NewRelic Alerts configurator constructor
     *
     * @param apiKey API Key for given NewRelic account
     */
    public Configurator(@NonNull String apiKey) {
        NewRelicApi api = new NewRelicApi(apiKey);
        applicationConfigurator = new ApplicationConfigurator(api);
        policyConfigurator = new PolicyConfigurator(api);
        conditionConfigurator = new ConditionConfigurator(api, EntityResolver.defaultInstance());
        nrqlConditionConfigurator = new NrqlConditionConfigurator(api);
        externalServiceConditionConfigurator = new ExternalServiceConditionConfigurator(api, EntityResolver.defaultInstance());
        channelConfigurator = new ChannelConfigurator(api);
    }

    Configurator(ApplicationConfigurator applicationConfigurator,
                 PolicyConfigurator policyConfigurator,
                 ConditionConfigurator conditionConfigurator,
                 NrqlConditionConfigurator nrqlConditionConfigurator,
                 ExternalServiceConditionConfigurator externalServiceConditionConfigurator,
                 ChannelConfigurator channelConfigurator) {
        this.applicationConfigurator = applicationConfigurator;
        this.policyConfigurator = policyConfigurator;
        this.conditionConfigurator = conditionConfigurator;
        this.nrqlConditionConfigurator = nrqlConditionConfigurator;
        this.externalServiceConditionConfigurator = externalServiceConditionConfigurator;
        this.channelConfigurator = channelConfigurator;
    }

    /**
     * Synchronizes configurations (see {@link Configurator#setApplicationConfigurations(Collection)}
     * and {@link Configurator#setPolicyConfigurations(Collection)})
     */
    public void sync() {
        for (ApplicationConfiguration applicationConfiguration : applicationConfigurations) {
            applicationConfigurator.sync(applicationConfiguration);
        }

        for (PolicyConfiguration configuration : policyConfigurations) {
            policyConfigurator.sync(configuration);
            conditionConfigurator.sync(configuration);
            nrqlConditionConfigurator.sync(configuration);
            externalServiceConditionConfigurator.sync(configuration);
            nrqlConditionConfigurator.sync(configuration);
            channelConfigurator.sync(configuration);
        }
    }

    /**
     * Sets application configurations to synchronize. It will only update configurations that are already defined in NewRelic.
     * It will not create new application configurations.
     *
     * @param applicationConfigurations collection of application configurations to synchronize
     * @see ApplicationConfiguration
     */
    public void setApplicationConfigurations(@NonNull Collection<ApplicationConfiguration> applicationConfigurations) {
        this.applicationConfigurations = applicationConfigurations;
    }

    /**
     * Sets policy configurations to synchronize. If policy with the same name is already defined in NewRelic, it will be
     * updated. New policy will be created otherwise.
     *
     * @param policyConfigurations collection of policy configurations to synchronize
     * @see PolicyConfiguration
     */
    public void setPolicyConfigurations(@NonNull Collection<PolicyConfiguration> policyConfigurations) {
        this.policyConfigurations = policyConfigurations;
    }
}
