package com.ocadotechnology.newrelic.alertsconfigurator.examples.configurer;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.ApplicationConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.examples.ApplicationConfigurator;
import com.ocadotechnology.newrelic.alertsconfigurator.examples.Defaults;

/**
 * Configuration of Application3 deployed on app-3-host
 * <p>
 * According to defined goals it should:
 *
 * <ul>
 *
 * <li> Raise warning alert if <b>apdex</b> falls below {@code 0.85} </li>
 * <li> Raise critical alert if <b>apdex</b> falls below {@code 0.7} </li>
 * <li> Send alert notification to <b>slack</b> to {@code newrelic-alerts} channel </li>
 * <li> Raise warning alert if <b>cpu usage</b> on application host raised above {@code 70%} </li>
 * <li> Raise critical alert if <b>cpu usage</b> on application host raised above {@code 90%} </li>
 * <li> Send alert notifications to <b>email</b> {@code my-team@my-company.com} </li>
 *
 * </ul>
 */
public class Application3Configurator implements ApplicationConfigurator {

    private static final String APPLICATION_NAME = "Application3";

    @Override
    public ApplicationConfiguration getApplicationConfiguration() {
        return Defaults.applicationConfiguration(APPLICATION_NAME);
    }

    @Override
    public PolicyConfiguration getPolicyConfigurations() {
        return PolicyConfiguration.builder()
                .policyName("Application3 Policy")
                .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
                .condition(Defaults.apdexCondition(APPLICATION_NAME))
                .condition(Defaults.cpuUsageCondition("app-3-host"))
                .condition(Defaults.heapUsageCondition(APPLICATION_NAME))
                .channel(Defaults.teamEmailChannel())
                .channel(Defaults.slackChannel())
                .build();
    }
}
