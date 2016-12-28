package com.ocado.panda.newrelic.api.internal;

import com.ocado.panda.newrelic.api.AlertsChannelsApi;
import com.ocado.panda.newrelic.api.AlertsConditionsApi;
import com.ocado.panda.newrelic.api.AlertsExternalServiceConditionsApi;
import com.ocado.panda.newrelic.api.AlertsPoliciesApi;
import com.ocado.panda.newrelic.api.ApplicationsApi;
import com.ocado.panda.newrelic.api.DeploymentsApi;
import com.ocado.panda.newrelic.api.KeyTransactionsApi;
import com.ocado.panda.newrelic.api.ServersApi;
import com.ocado.panda.newrelic.api.internal.client.NewRelicClient;
import lombok.Getter;

/**
 * API facade - object exposing NewRelic API endpoints as Java methods. Requires API key.
 */
@Getter
public class NewRelicInternalApi {

    private static final String NEWRELIC_HOST_URL = "https://api.newrelic.com";

    private final ApplicationsApi applicationsApi;

    private final AlertsChannelsApi alertsChannelsApi;

    private final AlertsPoliciesApi alertsPoliciesApi;

    private final AlertsConditionsApi alertsConditionsApi;

    private final AlertsExternalServiceConditionsApi alertsExternalServiceConditionsApi;

    private final KeyTransactionsApi keyTransactionsApi;

    private final DeploymentsApi deploymentsApi;

    private final ServersApi serversApi;

    /**
     * NewRelic API constructor.
     *
     * @param hostUrl NewRelic API host URL, for example https://api.newrelic.com
     * @param apiKey  API Key for given NewRelic account
     */
    public NewRelicInternalApi(String hostUrl, String apiKey) {
        NewRelicClient client = new NewRelicClient(hostUrl, apiKey);
        applicationsApi = new DefaultApplicationsApi(client);
        alertsChannelsApi = new DefaultAlertsChannelsApi(client);
        alertsPoliciesApi = new DefaultAlertsPoliciesApi(client);
        alertsConditionsApi = new DefaultAlertsConditionsApi(client);
        alertsExternalServiceConditionsApi = new DefaultAlertsExternalServiceConditionsApi(client);
        keyTransactionsApi = new DefaultKeyTransactionsApi(client);
        deploymentsApi = new DefaultDeploymentsApi(client);
        serversApi = new DefaultServersApi(client);
    }
}
