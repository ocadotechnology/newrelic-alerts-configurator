package com.ocado.panda.newrelic.sync;

import com.ocado.panda.newrelic.api.NewRelicApi;
import com.ocado.panda.newrelic.api.model.applications.Application;
import com.ocado.panda.newrelic.api.model.applications.ApplicationSettings;
import com.ocado.panda.newrelic.sync.configuration.ApplicationConfiguration;
import com.ocado.panda.newrelic.sync.exception.NewRelicSyncException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static java.lang.String.format;

@Slf4j
class ApplicationSynchronizer {
    private final NewRelicApi api;

    ApplicationSynchronizer(@NonNull NewRelicApi api) {
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
