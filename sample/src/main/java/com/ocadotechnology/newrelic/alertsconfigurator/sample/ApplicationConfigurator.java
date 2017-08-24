package com.ocadotechnology.newrelic.alertsconfigurator.sample;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.ApplicationConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;

/**
 * Returns configuration for one application in NewRelic. In this sample, each application will have one application
 * configuration and one policy configuration
 */
public interface ApplicationConfigurator {

    ApplicationConfiguration getApplicationConfiguration();

    PolicyConfiguration getPolicyConfigurations();
}
