package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.ApplicationConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.exception.NewRelicSyncException;
import com.ocadotechnology.newrelic.apiclient.NewRelicApi;
import com.ocadotechnology.newrelic.apiclient.model.applications.Application;
import com.ocadotechnology.newrelic.apiclient.model.applications.ApplicationSettings;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static java.lang.String.format;

@Slf4j
class ApplicationConfigurator {
    private final NewRelicApi api;

    ApplicationConfigurator(@NonNull NewRelicApi api) {
        this.api = api;
    }

    void sync(@NonNull ApplicationConfiguration config) {
        LOG.info("Synchronizing application {}...", config.getApplicationName());

        Application application = api.getApplicationsApi().getByName(config.getApplicationName()).orElseThrow(
            () -> new NewRelicSyncException(format("Application %s does not exist", config.getApplicationName())));

        ApplicationSettings settings = ApplicationSettings.builder()
            .appApdexThreshold(config.getAppApdexThreshold())
            .endUserApdexThreshold(config.getEndUserApdexThreshold())
            .enableRealUserMonitoring(config.isEnableRealUserMonitoring())
            .build();

        Application applicationUpdate = Application.builder()
            .name(config.getApplicationName())
            .settings(settings)
            .build();

        api.getApplicationsApi().update(application.getId(), applicationUpdate);
        LOG.info("Application {} synchronized", config.getApplicationName());
    }

}
