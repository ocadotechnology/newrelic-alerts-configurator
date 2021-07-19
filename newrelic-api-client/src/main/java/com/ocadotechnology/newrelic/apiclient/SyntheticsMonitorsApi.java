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

    /**
     * Create a new {@link com.ocadotechnology.newrelic.apiclient.model.synthetics.Monitor}.
     * <p>
     *
     *
     * @param monitor to be created in NewRelic
     * @return created {@link com.ocadotechnology.newrelic.apiclient.model.synthetics.Monitor}
     */
    Monitor create(Monitor monitor) throws Exception;

    /**
     * Deletes a {@link com.ocadotechnology.newrelic.apiclient.model.synthetics.Monitor}.
     * <p>
     *
     *
     * @param monitor to be deleted in NewRelic
     * @return deleted {@link com.ocadotechnology.newrelic.apiclient.model.synthetics.Monitor}
     */
    Monitor delete(Monitor monitor);

}
