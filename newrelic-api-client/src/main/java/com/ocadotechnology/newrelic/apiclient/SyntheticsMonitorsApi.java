package com.ocadotechnology.newrelic.apiclient;

import java.util.Optional;

import com.ocadotechnology.newrelic.apiclient.model.synthetics.Monitor;

public interface SyntheticsMonitorsApi {

    /**
     * Gets {@link Monitor} object using its name.
     *
     * @param monitorName name of the monitor created in Synthetics
     * @return Optional containing {@link Monitor} object, or empty if not found
     */
    Optional<Monitor> getByName(String monitorName);

}
