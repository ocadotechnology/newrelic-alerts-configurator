package com.ocadotechnology.newrelic.graphql.apiclient;

import com.ocadotechnology.newrelic.apiclient.*;
import com.ocadotechnology.newrelic.apiclient.internal.*;
import com.ocadotechnology.newrelic.apiclient.internal.client.NewRelicClient;
import com.ocadotechnology.newrelic.apiclient.internal.client.NewRelicClientFactory;
import com.ocadotechnology.newrelic.apiclient.model.conditions.AlertsCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.external.AlertsExternalServiceCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.AlertsNrqlCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.synthetics.AlertsSyntheticsCondition;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.*;
import lombok.Getter;

/**
 * API facade - object exposing NewRelic GraphQL API as Java methods. Requires API key and NewRelic account id.
 */
@Getter
public class NewRelicHybridApi implements NewRelicApi {
    private static final String GRAPHQL_API_URL = "https://api.newrelic.com/graphql";
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

    private final DashboardsApi dashboardsApi;

    private final SyntheticsMonitorsApi syntheticsMonitorsApi;

    /**
     * NewRelic Hybrid API constructor.
     *
     * @param apiKey           API Key for given NewRelic account
     * @param accountId        NewRelic account id
     */
    public NewRelicHybridApi(String apiKey, long accountId) {
        this(GRAPHQL_API_URL, REST_API_URL, SYNTHETICS_URL, new NewRelicClientFactory(apiKey), accountId);
    }

    /**
     * NewRelic Hybrid API constructor.
     *
     * @param graphqlApiUrl    NewRelic GRAPH QL API URL, for example https://api.newrelic.com/graphql
     * @param restApiUrl       NewRelic REST API URL, for example https://api.newrelic.com
     * @param syntheticsApiUrl NewRelic Synthetics API URL
     * @param clientFactory    NewRelic client factory
     * @param accountId        NewRelic account id
     */
    public NewRelicHybridApi(String graphqlApiUrl, String restApiUrl, String syntheticsApiUrl, NewRelicClientFactory clientFactory, long accountId) {
        NewRelicClient graphqlClient = clientFactory.create(graphqlApiUrl);
        NewRelicClient restClient = clientFactory.create(restApiUrl);
        NewRelicClient syntheticsClient = clientFactory.create(syntheticsApiUrl);
        DefaultQueryExecutor queryExecutor = new DefaultQueryExecutor(graphqlClient);
        ModelTranslator modelTranslator = new ModelTranslator();

        this.alertsChannelsApi = new GraphqlAlertsChannelsApi(queryExecutor, accountId, modelTranslator);
        this.alertsPoliciesApi = new GraphqlAlertsPoliciesApi(queryExecutor, accountId);
        this.alertsNrqlConditionsApi = new GraphqlAlertsNrqlConditionsApi(queryExecutor, accountId, modelTranslator);

        // To be replaced by NRQL conditions
        this.alertsConditionsApi = new DefaultAlertsConditionsApi(restClient);
        this.alertsSyntheticsConditionApi = new DefaultAlertsSyntheticsConditionsApi(restClient);
        this.alertsExternalServiceConditionsApi = new DefaultAlertsExternalServiceConditionsApi(restClient);

        // Still valid rest API with no replacement
        this.applicationsApi = new DefaultApplicationsApi(restClient);
        this.keyTransactionsApi = new DefaultKeyTransactionsApi(restClient);
        this.deploymentsApi = new DefaultDeploymentsApi(restClient);

        // To be deleted
        this.serversApi = new DefaultServersApi(restClient);
        this.usersApi = new DefaultUsersApi(restClient);
        this.dashboardsApi = new DefaultDashboardsApi(restClient);

        // To be replaced but with changes
        this.syntheticsMonitorsApi = new DefaultSyntheticsMonitorsApi(syntheticsClient);
    }
}
