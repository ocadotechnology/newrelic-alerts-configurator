package com.ocadotechnology.newrelic.alertsconfigurator.examples;

import com.ocadotechnology.newrelic.alertsconfigurator.Configurator;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.ApplicationConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.DashboardConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.dashboard.DashboardEditable;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.dashboard.DashboardIcon;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.dashboard.DashboardVisibility;
import com.ocadotechnology.newrelic.alertsconfigurator.examples.configurer.Application1Configurator;
import com.ocadotechnology.newrelic.alertsconfigurator.examples.configurer.Application2Configurator;
import com.ocadotechnology.newrelic.alertsconfigurator.examples.configurer.Application3Configurator;
import jersey.repackaged.com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.ocadotechnology.newrelic.alertsconfigurator.examples.Defaults.errorCountDashboard;

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

        List<DashboardConfiguration> dashboardConfigurations = getDashboardConfigurations();

        synchronize(newRelicApiKey, applicationConfigurations, policyConfigurations, dashboardConfigurations);
    }

    private static void synchronize(String newRelicApiKey, List<ApplicationConfiguration> applicationConfigurations,
                                    List<PolicyConfiguration> policyConfigurations, List<DashboardConfiguration> dashboardConfigurations) {
        Configurator configurator = new Configurator(newRelicApiKey);
        configurator.setApplicationConfigurations(applicationConfigurations);
        configurator.setPolicyConfigurations(policyConfigurations);
        configurator.setDashboardConfigurations(dashboardConfigurations);
        configurator.sync();
    }

    private static List<ApplicationConfigurator> getApplicationConfigurers() {
        return Arrays.asList(
                new Application1Configurator(),
                new Application2Configurator(),
                new Application3Configurator()
        );
    }

    private static List<DashboardConfiguration> getDashboardConfigurations() {
        return ImmutableList.of(
                DashboardConfiguration.builder()
                        .title("Application 2 dashboard")
                        .description("Details of app 2")
                        .icon(DashboardIcon.BELL)
                        .visibility(DashboardVisibility.ALL)
                        .editable(DashboardEditable.EDITABLE_BY_OWNER)
                        .ownerEmail("tester@testee.local")
                        .widgets(ImmutableList.of(errorCountDashboard()))
                        .build()
        );
    }
}
