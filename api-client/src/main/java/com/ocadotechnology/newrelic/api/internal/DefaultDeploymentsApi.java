package com.ocadotechnology.newrelic.api.internal;

import com.ocadotechnology.newrelic.api.DeploymentsApi;
import com.ocadotechnology.newrelic.api.internal.client.NewRelicClient;
import com.ocadotechnology.newrelic.api.internal.model.DeploymentWrapper;
import com.ocadotechnology.newrelic.api.internal.model.DeploymentsList;
import com.ocadotechnology.newrelic.api.model.deployments.Deployment;

import javax.ws.rs.client.Entity;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

class DefaultDeploymentsApi extends ApiBase implements DeploymentsApi {

    private static final String DEPLOYMENTS_URL = "/v2/applications/{application_id}/deployments.json";
    private static final String DEPLOYMENT_URL = "/v2/applications/{application_id}/deployments/{deployment_id}.json";

    DefaultDeploymentsApi(NewRelicClient client) {
        super(client);
    }

    @Override
    public List<Deployment> list(int applicationId) {
        return getPageable(
                client.target(DEPLOYMENTS_URL)
                        .resolveTemplate("application_id", applicationId)
                        .request(APPLICATION_JSON_TYPE),
                DeploymentsList.class)
                .getList();
    }

    @Override
    public Deployment create(int applicationId, Deployment deployment) {
        return client
                .target(DEPLOYMENTS_URL)
                .resolveTemplate("application_id", applicationId)
                .request(APPLICATION_JSON_TYPE)
                .post(Entity.entity(new DeploymentWrapper(deployment), APPLICATION_JSON_TYPE), DeploymentWrapper.class)
                .getDeployment();
    }

    @Override
    public Deployment delete(int applicationId, int deploymentId) {
        return client
                .target(DEPLOYMENT_URL)
                .resolveTemplate("application_id", applicationId)
                .resolveTemplate("deployment_id", deploymentId)
                .request(APPLICATION_JSON_TYPE)
                .delete(DeploymentWrapper.class)
                .getDeployment();
    }
}
