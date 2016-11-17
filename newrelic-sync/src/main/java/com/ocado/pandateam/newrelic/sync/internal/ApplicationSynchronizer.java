package com.ocado.pandateam.newrelic.sync.internal;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.applications.Application;
import com.ocado.pandateam.newrelic.api.model.applications.Settings;
import com.ocado.pandateam.newrelic.sync.configuration.ApplicationConfiguration;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;
import lombok.AllArgsConstructor;

import java.util.Optional;

import static java.lang.String.format;

@AllArgsConstructor
public class ApplicationSynchronizer {

    private final NewRelicApi api;
    private final ApplicationConfiguration config;

    public void sync() throws NewRelicApiException, NewRelicSyncException {
        if (api == null) {
            throw new NewRelicSyncException("Initialization error");
        }

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
    }

}
