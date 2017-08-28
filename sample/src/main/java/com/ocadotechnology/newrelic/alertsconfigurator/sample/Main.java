package com.ocadotechnology.newrelic.alertsconfigurator.sample;

import com.ocadotechnology.newrelic.alertsconfigurator.Configurator;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.ApplicationConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.sample.configurer.Application1Configurator;
import com.ocadotechnology.newrelic.alertsconfigurator.sample.configurer.Application2Configurator;
import com.ocadotechnology.newrelic.alertsconfigurator.sample.configurer.Application3Configurator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static final String STAGING_ENVIRONMENT_NEWRELIC_KEY = "KEY1";
    public static final String PRODUCTION_ENVIRONMENT_NEWRELIC_KEY = "KEY2";

    public static void main(String[] args) {
        List<ApplicationConfigurator> appConfigurers = getApplicationConfigurers();

        configureAlerts(STAGING_ENVIRONMENT_NEWRELIC_KEY, appConfigurers);
        configureAlerts(PRODUCTION_ENVIRONMENT_NEWRELIC_KEY, appConfigurers);
    }

    private static void configureAlerts(String newRelicApiKey, List<ApplicationConfigurator> applicationConfigurers) {
        List<ApplicationConfiguration> applicationConfigurations = applicationConfigurers.stream()
                .map(ApplicationConfigurator::getApplicationConfiguration)
                .collect(Collectors.toList());
        List<PolicyConfiguration> policyConfigurations = applicationConfigurers.stream()
                .map(ApplicationConfigurator::getPolicyConfigurations)
                .collect(Collectors.toList());

        synchronizeAlerts(newRelicApiKey, applicationConfigurations, policyConfigurations);
    }

    private static void synchronizeAlerts(String newRelicApiKey, List<ApplicationConfiguration> applicationConfigurations,
                                          List<PolicyConfiguration> policyConfigurations) {
        Configurator configurator = new Configurator(newRelicApiKey);
        configurator.setApplicationConfigurations(applicationConfigurations);
        configurator.setPolicyConfigurations(policyConfigurations);
        configurator.sync();
    }

    private static List<ApplicationConfigurator> getApplicationConfigurers() {
        return Arrays.asList(
                new Application1Configurator(),
                new Application2Configurator(),
                new Application3Configurator()
        );
    }
}
