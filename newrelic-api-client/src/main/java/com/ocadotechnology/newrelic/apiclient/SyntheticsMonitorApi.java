package com.ocadotechnology.newrelic.apiclient;

import java.util.Optional;

import com.ocadotechnology.newrelic.apiclient.model.applications.Application;

public interface SyntheticsMonitorApi {
    /**
     * Gets {@link Application} object using its name.
     *
     * @param applicationName name of the application registered in NewRelic
     * @return Optional containing {@link Application} object, or empty if application not found
     */
    Optional<Application> getByName(String applicationName);

}
