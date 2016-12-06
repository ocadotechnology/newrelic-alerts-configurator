package com.ocado.panda.newrelic.api.internal;

import com.ocado.panda.newrelic.api.ApplicationsApi;
import com.ocado.panda.newrelic.api.internal.client.NewRelicClient;
import com.ocado.panda.newrelic.api.internal.model.ApplicationList;
import com.ocado.panda.newrelic.api.internal.model.ApplicationWrapper;
import com.ocado.panda.newrelic.api.model.applications.Application;

import javax.ws.rs.client.Entity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

class DefaultApplicationsApi extends ApiBase implements ApplicationsApi {

    private static final String APPLICATIONS_URL = "/v2/applications.json";
    private static final String APPLICATION_URL = "/v2/applications/{application_id}.json";

    DefaultApplicationsApi(NewRelicClient client) {
        super(client);
    }

    @Override
    public Optional<Application> getByName(String applicationName) {
        ApplicationList applicationList = client
                .target(APPLICATIONS_URL)
                .queryParam("filter[name]", applicationName)
                .request(APPLICATION_JSON_TYPE)
                .get(ApplicationList.class);

        List<Application> applications = applicationList.getList().stream()
                .filter(application -> application.getName().equals(applicationName))
                .collect(Collectors.toList());

        applicationList = new ApplicationList(applications);

        return applicationList.getSingle();
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
