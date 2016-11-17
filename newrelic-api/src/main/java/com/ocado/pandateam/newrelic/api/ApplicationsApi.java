package com.ocado.pandateam.newrelic.api;

import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.RequestBodyEntity;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.NewRelicRestClient;
import com.ocado.pandateam.newrelic.api.model.applications.Application;
import com.ocado.pandateam.newrelic.api.model.applications.ApplicationList;
import com.ocado.pandateam.newrelic.api.model.applications.ApplicationWrapper;

import java.util.Optional;

public class ApplicationsApi extends BaseApi {

    private static final String APPLICATIONS_URL = "/v2/applications.json";
    private static final String APPLICATION_URL = "/v2/applications/{application_id}.json";

    ApplicationsApi(NewRelicRestClient api) {
        super(api);
    }

    /**
     * Get {@link Application} object using its name.
     *
     * @param name Name of the application registered in NewRelic
     * @return Optional containing {@link Application} object, or empty if application not found
     * @throws NewRelicApiException when more than one response returned or received error response
     */
    public Optional<Application> getByName(String name) throws NewRelicApiException {
        HttpRequest request = api.get(APPLICATIONS_URL).queryString("filter[name]", name);
        return api.asObject(request, ApplicationList.class).getSingle();
    }

    /**
     * Updates {@link Application} object.
     *
     * @param application Application to be updated.
     * @return Updated {@link Application}.
     * @throws NewRelicApiException when received error response
     */
    public Application update(int applicationId, Application application) throws NewRelicApiException {
        RequestBodyEntity request = api.put(APPLICATION_URL)
                .routeParam("application_id", String.valueOf(applicationId))
                .body(new ApplicationWrapper(application));
        return api.asObject(request, ApplicationWrapper.class).getApplication();
    }
}
