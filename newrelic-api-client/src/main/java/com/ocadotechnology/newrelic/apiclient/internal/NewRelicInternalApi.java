package com.ocadotechnology.newrelic.apiclient.internal;

import com.ocadotechnology.newrelic.apiclient.*;
import com.ocadotechnology.newrelic.apiclient.internal.client.NewRelicClient;
import com.ocadotechnology.newrelic.apiclient.model.conditions.AlertsCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.external.AlertsExternalServiceCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.AlertsNrqlCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.synthetics.AlertsSyntheticsCondition;
import lombok.Getter;

/**
 * API facade - object exposing NewRelic API endpoints as Java methods. Requires API key.
 */
@Getter
public class NewRelicInternalApi {

    private final ApplicationsApi applicationsApi;

    private final AlertsChannelsApi alertsChannelsApi;

    private final AlertsPoliciesApi alertsPoliciesApi;

    private final PolicyItemApi<AlertsCondition> alertsConditionsApi;

    private final PolicyItemApi<AlertsNrqlCondition> alertsNrqlConditionsApi;

    private final PolicyItemApi<AlertsExternalServiceCondition> alertsExternalServiceConditionsApi;

    private final PolicyItemApi<AlertsSyntheticsCondition> alertsSyntheticsConditionApi;

    private final KeyTransactionsApi keyTransactionsApi;

    private final DeploymentsApi deploymentsApi;

    private final ServersApi serversApi;

    private final UsersApi usersApi;

    private final SyntheticsMonitorsApi syntheticsMonitorsApi;

    private final DashboardsApi dashboardsApi;

    /**
     * NewRelic API constructor.
     *
     * @param restApiUrl       NewRelic REST API URL, for example https://api.newrelic.com
     * @param syntheticsApiUrl NewRelic Synthetics API URL
     * @param apiKey           API Key for given NewRelic account
     */
    public NewRelicInternalApi(String restApiUrl, String syntheticsApiUrl, String apiKey) {
        NewRelicClient client = new NewRelicClient(restApiUrl, apiKey);
        applicationsApi = new DefaultApplicationsApi(client);
        alertsChannelsApi = new DefaultAlertsChannelsApi(client);
        alertsPoliciesApi = new DefaultAlertsPoliciesApi(client);
        alertsConditionsApi = new DefaultAlertsConditionsApi(client);
        alertsExternalServiceConditionsApi = new DefaultAlertsExternalServiceConditionsApi(client);
        alertsNrqlConditionsApi = new DefaultAlertsNrqlConditionsApi(client);
        alertsSyntheticsConditionApi = new DefaultAlertsSyntheticsConditionsApi(client);
        keyTransactionsApi = new DefaultKeyTransactionsApi(client);
        deploymentsApi = new DefaultDeploymentsApi(client);
        serversApi = new DefaultServersApi(client);
        usersApi = new DefaultUsersApi(client);
        dashboardsApi = new DefaultDashboardsApi(client);

        NewRelicClient syntheticsClient = new NewRelicClient(syntheticsApiUrl, apiKey);
        syntheticsMonitorsApi = new DefaultSyntheticsMonitorsApi(syntheticsClient);
    }
}
