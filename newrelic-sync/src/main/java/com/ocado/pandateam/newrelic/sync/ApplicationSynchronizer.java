package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.model.applications.Application;
import com.ocado.pandateam.newrelic.api.model.applications.Settings;
import com.ocado.pandateam.newrelic.sync.configuration.ApplicationConfiguration;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;
import lombok.NonNull;

import java.util.Optional;
import java.util.logging.Logger;

import static java.lang.String.format;

class ApplicationSynchronizer {
    private static final Logger LOG = Logger.getLogger(ApplicationSynchronizer.class.getName());

    private final NewRelicApi api;
    private final ApplicationConfiguration config;

    ApplicationSynchronizer(@NonNull NewRelicApi api, @NonNull ApplicationConfiguration config) {
        this.api = api;
        this.config = config;
    }

    void sync() {
        LOG.info(format("Synchronizing application %s...", config.getApplicationName()));

        Optional<Application> applicationOptional = api.getApplicationsApi().getByName(config.getApplicationName());
        Application application = applicationOptional.orElseThrow(
            () -> new NewRelicSyncException(format("Application %s does not exist", config.getApplicationName())));

        Settings settings = Settings.builder()
            .appApdexThreshold(config.getAppApdexThreshold())
            .endUserApdexThreshold(config.getEndUserApdexThreshold())
            .enableRealUserMonitoring(config.isEnableRealUserMonitoring())
            .build();

        Application applicationUpdate = Application.builder()
            .name(config.getApplicationName())
            .settings(settings)
            .build();

        api.getApplicationsApi().update(application.getId(), applicationUpdate);
        LOG.info(format("Application %s synchronized!", config.getApplicationName()));
    }

}
