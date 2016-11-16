package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.applications.Application;
import com.ocado.pandateam.newrelic.api.model.applications.Settings;
import com.ocado.pandateam.newrelic.sync.configuration.ApplicationConfiguration;

import java.util.Optional;

public class ApplicationSynchronizer {

    private final NewRelicApi api;

    private final ApplicationConfiguration config;

    public ApplicationSynchronizer(NewRelicApi api, ApplicationConfiguration config) {
        this.api = api;
        this.config = config;
    }

    public void sync() throws NewRelicApiException, NewRelicSyncException {
        Optional<Application> applicationOptional = api.getApplicationsApi().getByName(config.getApplicationName());

        Application application = applicationOptional.orElseThrow(NewRelicSyncException::new);

        Settings settings = Settings.builder()
                .appApdexThreshold(config.getAppApdexThreshold())
                .endUserApdexThreshold(config.getUserApdexThreshold())
                .build();
        Application applicationUpdate = Application.builder()
                .name(config.getApplicationName())
                .settings(settings)
                .build();
        api.getApplicationsApi().update(application.getId(), applicationUpdate);
    }

}
