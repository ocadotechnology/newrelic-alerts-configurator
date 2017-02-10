package com.ocado.panda.newrelic.deployment;

import com.ocado.newrelic.api.NewRelicApi;
import com.ocado.newrelic.api.model.applications.Application;
import com.ocado.newrelic.api.model.deployments.Deployment;
import lombok.NonNull;

import java.util.List;

public class NewRelicDeployment {

    private final NewRelicApi api;

    public NewRelicDeployment(@NonNull String apiKey) {
        api = new NewRelicApi(apiKey);
    }

    private Application getApplicationByName(String applicationName) {
        return api.getApplicationsApi()
                .getByName(applicationName)
                .orElseThrow(() -> new RuntimeException("Application not found: " + applicationName));
    }

    public List<Deployment> list(String applicationName) {
        int applicationId = getApplicationByName(sanitize(applicationName)).getId();
        return api.getDeploymentsApi().list(applicationId);
    }

    public Deployment mark(String applicationName, Deployment deployment) {
        int applicationId = getApplicationByName(sanitize(applicationName)).getId();
        return api.getDeploymentsApi().create(applicationId, deployment);
    }

    public Deployment remove(String applicationName, Integer deploymentId) {
        int applicationId = getApplicationByName(sanitize(applicationName)).getId();
        return api.getDeploymentsApi().delete(applicationId, deploymentId);
    }

    private static String sanitize(String input) {
        return input.replace('{', '(').replace('}', ')');
    }

}
