package com.ocadotechnology.newrelic.api.internal;

import com.ocadotechnology.newrelic.api.ApplicationsApi;
import com.ocadotechnology.newrelic.api.internal.client.NewRelicClient;
import com.ocadotechnology.newrelic.api.internal.model.ApplicationList;
import com.ocadotechnology.newrelic.api.internal.model.ApplicationWrapper;
import com.ocadotechnology.newrelic.api.model.applications.Application;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

class DefaultApplicationsApi extends ApiBase implements ApplicationsApi {

    private static final String APPLICATIONS_URL = "/v2/applications.json";
    private static final String APPLICATION_URL = "/v2/applications/{application_id}.json";

    DefaultApplicationsApi(NewRelicClient client) {
        super(client);
    }

    @Override
    public Optional<Application> getByName(String applicationName) {
        Invocation.Builder builder = client
                .target(APPLICATIONS_URL)
                .queryParam("filter[name]", applicationName)
                .request(APPLICATION_JSON_TYPE);
        return getPageable(builder, ApplicationList.class)
                .filter(application -> application.getName().equals(applicationName))
                .getSingle();
    }

    @Override
    public Application update(int applicationId, Application application) {
        return client
                .target(APPLICATION_URL)
                .resolveTemplate("application_id", applicationId)
                .request(APPLICATION_JSON_TYPE)
                .put(Entity.entity(new ApplicationWrapper(application), APPLICATION_JSON_TYPE), ApplicationWrapper.class)
                .getApplication();
    }
}
