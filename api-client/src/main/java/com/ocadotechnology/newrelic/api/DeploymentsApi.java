package com.ocadotechnology.newrelic.api;

import com.ocadotechnology.newrelic.api.model.deployments.Deployment;

import java.util.List;

public interface DeploymentsApi {

    /**
     * Lists all application deployments.
     * This method supports pagination - which means it returns list of entries combined from all pages.
     *
     * @param applicationId id of the application
     * @return list of all application {@link Deployment} from NewRelic
     */
    List<Deployment> list(int applicationId);

    /**
     * Creates deployment for application.
     *
     * @param applicationId id of the application
     * @param deployment deployment definition to be created
     * @return created {@link Deployment}
     */
    Deployment create(int applicationId, Deployment deployment);

    /**
     * Removes deployment from application deployment history.
     *
     * @param applicationId id of the application
     * @param deploymentId deployment definition to be deleted
     * @return {@link Deployment} for the given deployment id and application id.
     */
    Deployment delete(int applicationId, int deploymentId);
}
