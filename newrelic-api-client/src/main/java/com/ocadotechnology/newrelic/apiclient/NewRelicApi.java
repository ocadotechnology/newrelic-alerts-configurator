package com.ocadotechnology.newrelic.apiclient;

import com.ocadotechnology.newrelic.apiclient.model.conditions.AlertsCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.external.AlertsExternalServiceCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.AlertsNrqlCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.synthetics.AlertsSyntheticsCondition;

/**
 * API facade - interface exposing NewRelic API as Java methods.
 */
public interface NewRelicApi {

    ApplicationsApi getApplicationsApi();

    AlertsChannelsApi getAlertsChannelsApi();

    AlertsPoliciesApi getAlertsPoliciesApi();

    PolicyItemApi<AlertsCondition> getAlertsConditionsApi();

    PolicyItemApi<AlertsNrqlCondition> getAlertsNrqlConditionsApi();

    PolicyItemApi<AlertsExternalServiceCondition> getAlertsExternalServiceConditionsApi();

    PolicyItemApi<AlertsSyntheticsCondition> getAlertsSyntheticsConditionApi();

    KeyTransactionsApi getKeyTransactionsApi();

    DeploymentsApi getDeploymentsApi();

    ServersApi getServersApi();

    UsersApi getUsersApi();

    DashboardsApi getDashboardsApi();

    SyntheticsMonitorsApi getSyntheticsMonitorsApi();
}
