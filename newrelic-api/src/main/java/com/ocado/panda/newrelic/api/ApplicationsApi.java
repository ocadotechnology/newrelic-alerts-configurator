package com.ocado.panda.newrelic.api;

import com.ocado.panda.newrelic.api.model.applications.Application;

import java.util.Optional;

public interface ApplicationsApi {
    /**
     * Gets {@link Application} object using its name.
     *
     * @param applicationName name of the application registered in NewRelic
     * @return Optional containing {@link Application} object, or empty if application not found
     */
    Optional<Application> getByName(String applicationName);

    /**
     * Updates {@link Application} object.
     *
     * @param applicationId id of the application to be updated
     * @param application   application definition to be updated
     * @return updated {@link Application}
     */
    Application update(int applicationId, Application application);
}
