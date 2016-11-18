package com.ocado.pandateam.newrelic.api.internal;

import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.RequestBodyEntity;
import com.ocado.pandateam.newrelic.api.ApplicationsApi;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.model.ApplicationList;
import com.ocado.pandateam.newrelic.api.internal.model.ApplicationWrapper;
import com.ocado.pandateam.newrelic.api.model.applications.Application;

import java.util.Optional;

public class DefaultApplicationsApi extends ApiBase implements ApplicationsApi {

    private static final String APPLICATIONS_URL = "/v2/applications.json";
    private static final String APPLICATION_URL = "/v2/applications/{application_id}.json";

    DefaultApplicationsApi(NewRelicRestClient api) {
        super(api);
    }

    @Override
    public Optional<Application> getByName(String name) throws NewRelicApiException {
        HttpRequest request = api.get(APPLICATIONS_URL).queryString("filter[name]", name);
        return api.asObject(request, ApplicationList.class).getSingle();
    }

    @Override
    public Application update(int applicationId, Application application) throws NewRelicApiException {
        RequestBodyEntity request = api.put(APPLICATION_URL)
                .routeParam("application_id", String.valueOf(applicationId))
                .body(new ApplicationWrapper(application));
        return api.asObject(request, ApplicationWrapper.class).getApplication();
    }
}
