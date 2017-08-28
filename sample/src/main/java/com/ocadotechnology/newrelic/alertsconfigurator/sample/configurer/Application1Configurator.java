package com.ocadotechnology.newrelic.alertsconfigurator.sample.configurer;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.ApplicationConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.sample.ApplicationConfigurator;
import com.ocadotechnology.newrelic.alertsconfigurator.sample.Defaults;

/**
 * Configuration of Application1 deployed on app-1-host
 * <p>
 * According to defined goals it should:
 *
 * <ul>
 *
 * <li> Raise warning alert if <b>apdex</b> falls below {@code 0.85} </li>
 * <li> Raise critical alert if <b>apdex</b> falls below {@code 0.7} </li>
 * <li> Send alert notification to <b>slack</b> to {@code newrelic-alerts} channel </li>
 * <li> Raise warning alert if <b>disk space</b> on application host raises above {@code 65%} </li>
 * <li> Raise critical alert if <b>disk space</b> on application host raises above {@code 80%} </li>
 * <li> Send alert notifications to <b>email</b> {@code my-team@my-company.com} </li>
 *
 * </ul>
 */
public class Application1Configurator implements ApplicationConfigurator {

    private static final String APPLICATION_NAME = "Application1";

    @Override
    public ApplicationConfiguration getApplicationConfiguration() {
        return Defaults.applicationConfiguration(APPLICATION_NAME);
    }

    @Override
    public PolicyConfiguration getPolicyConfigurations() {
        return PolicyConfiguration.builder()
                .policyName("Application1 Policy")
                .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
                .condition(Defaults.apdexCondition(APPLICATION_NAME))
                .condition(Defaults.diskSpaceCondition("app-1-host"))
                .channel(Defaults.teamEmailChannel())
                .channel(Defaults.slackChannel())
                .build();
    }
}
