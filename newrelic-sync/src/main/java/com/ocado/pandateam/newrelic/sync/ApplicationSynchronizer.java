package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.model.applications.Application;
import com.ocado.pandateam.newrelic.api.model.applications.ApplicationSettings;
import com.ocado.pandateam.newrelic.sync.configuration.ApplicationConfiguration;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static java.lang.String.format;

@Slf4j
class ApplicationSynchronizer {
    private final NewRelicApi api;
    private final ApplicationConfiguration config;

    ApplicationSynchronizer(@NonNull NewRelicApi api, @NonNull ApplicationConfiguration config) {
        this.api = api;
        this.config = config;
    }

    void sync() {
        LOG.info("Synchronizing application {}...", config.getApplicationName());

        Optional<Application> applicationOptional = api.getApplicationsApi().getByName(config.getApplicationName());
        Application application = applicationOptional.orElseThrow(
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
