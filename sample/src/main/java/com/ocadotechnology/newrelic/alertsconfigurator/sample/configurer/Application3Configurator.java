package com.ocadotechnology.newrelic.alertsconfigurator.sample.configurer;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.ApplicationConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.sample.ApplicationConfigurator;
import com.ocadotechnology.newrelic.alertsconfigurator.sample.Defaults;

/**
 * Configuration of Application3 deployed on app-3-host
 * <p>
 * According to defined goals it should:
 *
 * <ul>
 *
 * <li> Raise warning alert if <b>apdex</b> falls below <tt>0.85</tt> </li>
 * <li> Raise critical alert if <b>apdex</b> falls below <tt>0.7</tt> </li>
 * <li> Send alert notification to <b>slack</b> to <tt>newrelic-alerts</tt> channel </li>
 * <li> Raise warning alert if <b>cpu usage</b> on application host raised above <tt>70%</tt> </li>
 * <li> Raise critical alert if <b>cpu usage*</b> on application host raised above <tt>90%</tt> </li>
 * <li> Send alert notifications to <b>email</b> <tt>my-team@my-company.com</tt> </li>
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
                .policyName("Application2 Policy")
                .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
                .condition(Defaults.apdexCondition(APPLICATION_NAME))
                .condition(Defaults.cpuUsageCondition("app-3-host"))
                .channel(Defaults.teamEmailChannel())
                .channel(Defaults.slackChannel())
                .build();
    }
}
