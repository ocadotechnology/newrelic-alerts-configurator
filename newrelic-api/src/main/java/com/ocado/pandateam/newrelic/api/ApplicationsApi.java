package com.ocado.pandateam.newrelic.api;

import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.applications.Application;

import java.util.Optional;

public interface ApplicationsApi {
    /**
     * Gets {@link Application} object using its name.
     *
     * @param applicationName name of the application registered in NewRelic
     * @return Optional containing {@link Application} object, or empty if application not found
     * @throws NewRelicApiException when more than one response returned or received error response
     */
    Optional<Application> getByName(String applicationName) throws NewRelicApiException;

    /**
     * Updates {@link Application} object.
     *
     * @param applicationId id of the application to be updated
     * @param application   application definition to be updated
     * @return updated {@link Application}
     * @throws NewRelicApiException when received error response
     */
    Application update(int applicationId, Application application) throws NewRelicApiException;
}
