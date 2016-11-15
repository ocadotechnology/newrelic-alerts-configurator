package com.ocado.pandateam.newrelic.api;

import com.ocado.pandateam.newrelic.api.internal.NewRelicRestClient;
import lombok.Getter;

/**
 * Object exposing NewRelic API endpoints as Java methods. Requires API key.
 */
@Getter
public class NewRelicApi {

    private static final String NEWRELIC_HOST_URL = "https://api.newrelic.com";

    private final ApplicationsApi applicationsApi;

    private final AlertsChannelsApi alertsChannelsApi;

    private final AlertsPoliciesApi alertsPoliciesApi;

    private final AlertsConditionsApi alertsConditionsApi;

    private final AlertsExternalServiceConditionsApi alertsExternalServiceConditionsApi;

    /**
     * NewRelic API constructor.
     *
     * @param apiKey API Key for given NewRelic account
     */
    public NewRelicApi(String apiKey) {
        this(NEWRELIC_HOST_URL, apiKey);
    }

    /**
     * NewRelic API constructor.
     *
     * @param hostUrl NewRelic API host URL, for example https://api.newrelic.com
     * @param apiKey  API Key for given NewRelic account
     */
    public NewRelicApi(String hostUrl, String apiKey) {
        NewRelicRestClient client = new NewRelicRestClient(hostUrl, apiKey);
        applicationsApi = new ApplicationsApi(client);
        alertsChannelsApi = new AlertsChannelsApi(client);
        alertsPoliciesApi = new AlertsPoliciesApi(client);
        alertsConditionsApi = new AlertsConditionsApi(client);
        alertsExternalServiceConditionsApi = new AlertsExternalServiceConditionsApi(client);
    }
}
