package com.ocadotechnology.newrelic.apiclient;

import com.ocadotechnology.newrelic.apiclient.internal.NewRelicInternalApi;
import com.ocadotechnology.newrelic.apiclient.model.conditions.AlertsCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.AlertsNrqlCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.external.AlertsExternalServiceCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.synthetics.AlertsSyntheticsCondition;

import lombok.Getter;

/**
 * API facade - object exposing NewRelic API endpoints as Java methods. Requires API key.
 */
@Getter
public class NewRelicApi {

    private static final String REST_API_URL = "https://api.newrelic.com";
    private static final String SYNTHETICS_URL = "https://synthetics.newrelic.com/synthetics/api";

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

    /**
     * NewRelic API constructor.
     *
     * @param apiKey API Key for given NewRelic account
     */
    public NewRelicApi(String apiKey) {
        this(REST_API_URL, SYNTHETICS_URL, apiKey);
    }

    /**
     * NewRelic API constructor.
     *
     * @param restApiUrl NewRelic REST API URL, for example https://api.newrelic.com
     * @param syntheticsApiUrl NewRelic Synthetics API URL
     * @param apiKey  API Key for given NewRelic account
     */
    public NewRelicApi(String restApiUrl, String syntheticsApiUrl, String apiKey) {
        NewRelicInternalApi internalApi = new NewRelicInternalApi(restApiUrl, syntheticsApiUrl, apiKey);
        applicationsApi = internalApi.getApplicationsApi();
        alertsChannelsApi = internalApi.getAlertsChannelsApi();
        alertsPoliciesApi = internalApi.getAlertsPoliciesApi();
        alertsConditionsApi = internalApi.getAlertsConditionsApi();
        alertsExternalServiceConditionsApi = internalApi.getAlertsExternalServiceConditionsApi();
        alertsNrqlConditionsApi = internalApi.getAlertsNrqlConditionsApi();
        alertsSyntheticsConditionApi = internalApi.getAlertsSyntheticsConditionApi();
        keyTransactionsApi = internalApi.getKeyTransactionsApi();
        deploymentsApi = internalApi.getDeploymentsApi();
        serversApi = internalApi.getServersApi();
        usersApi = internalApi.getUsersApi();
        syntheticsMonitorsApi = internalApi.getSyntheticsMonitorsApi();
    }
}
