package com.ocadotechnology.newrelic.apiclient.internal;

import com.ocadotechnology.newrelic.apiclient.AlertsChannelsApi;
import com.ocadotechnology.newrelic.apiclient.AlertsConditionsApi;
import com.ocadotechnology.newrelic.apiclient.AlertsExternalServiceConditionsApi;
import com.ocadotechnology.newrelic.apiclient.AlertsNrqlConditionsApi;
import com.ocadotechnology.newrelic.apiclient.AlertsPoliciesApi;
import com.ocadotechnology.newrelic.apiclient.ApplicationsApi;
import com.ocadotechnology.newrelic.apiclient.DeploymentsApi;
import com.ocadotechnology.newrelic.apiclient.KeyTransactionsApi;
import com.ocadotechnology.newrelic.apiclient.ServersApi;
import com.ocadotechnology.newrelic.apiclient.UsersApi;
import com.ocadotechnology.newrelic.apiclient.internal.client.NewRelicClient;
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

    private final AlertsNrqlConditionsApi alertsNrqlConditionsApi;

    private final KeyTransactionsApi keyTransactionsApi;

    private final DeploymentsApi deploymentsApi;

    private final ServersApi serversApi;

    private final UsersApi usersApi;

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
        alertsNrqlConditionsApi = new DefaultAlertsNrqlConditionsApi(client);
        keyTransactionsApi = new DefaultKeyTransactionsApi(client);
        deploymentsApi = new DefaultDeploymentsApi(client);
        serversApi = new DefaultServersApi(client);
        usersApi = new DefaultUsersApi(client);
    }
}
