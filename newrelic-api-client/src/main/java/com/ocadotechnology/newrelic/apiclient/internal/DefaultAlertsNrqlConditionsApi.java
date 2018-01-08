package com.ocadotechnology.newrelic.apiclient.internal;

import com.ocadotechnology.newrelic.apiclient.AlertsNrqlConditionsApi;
import com.ocadotechnology.newrelic.apiclient.internal.client.NewRelicClient;
import com.ocadotechnology.newrelic.apiclient.internal.model.AlertsNrqlConditionList;
import com.ocadotechnology.newrelic.apiclient.internal.model.AlertsNrqlConditionWrapper;
import com.ocadotechnology.newrelic.apiclient.model.conditions.AlertsNrqlCondition;

import javax.ws.rs.client.Entity;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

class DefaultAlertsNrqlConditionsApi extends ApiBase implements AlertsNrqlConditionsApi {

    private static final String NRQL_CONDITIONS_URL = "/v2/alerts_nrql_conditions";
    private static final String NRQL_CONDITION_URL = "/v2/alerts_nrql_conditions/{condition_id}.json";
    private static final String NRQL_CONDITION_POLICY_URL = "/v2/alerts_nrql_conditions/policies/{policy_id}.json";

    DefaultAlertsNrqlConditionsApi(NewRelicClient client) {
        super(client);
    }

    @Override
    public List<AlertsNrqlCondition> list(int policyId) {
        return getPageable(
                client.target(NRQL_CONDITIONS_URL).queryParam("policy_id", policyId).request(APPLICATION_JSON_TYPE),
                AlertsNrqlConditionList.class)
                .getList();
    }

    @Override
    public AlertsNrqlCondition create(int policyId, AlertsNrqlCondition condition) {
        return client
                .target(NRQL_CONDITION_POLICY_URL)
                .resolveTemplate("policy_id", policyId)
                .request(APPLICATION_JSON_TYPE)
                .post(Entity.entity(new AlertsNrqlConditionWrapper(condition), APPLICATION_JSON_TYPE),
                        AlertsNrqlConditionWrapper.class)
                .getNrqlCondition();
    }

    @Override
    public AlertsNrqlCondition update(int conditionId, AlertsNrqlCondition condition) {
        return client
                .target(NRQL_CONDITION_URL)
                .resolveTemplate("condition_id", conditionId)
                .request(APPLICATION_JSON_TYPE)
                .put(Entity.entity(new AlertsNrqlConditionWrapper(condition), APPLICATION_JSON_TYPE),
                        AlertsNrqlConditionWrapper.class)
                .getNrqlCondition();
    }

    @Override
    public AlertsNrqlCondition delete(int conditionId) {
        return client
                .target(NRQL_CONDITION_URL)
                .resolveTemplate("condition_id", conditionId)
                .request(APPLICATION_JSON_TYPE)
                .delete(AlertsNrqlConditionWrapper.class)
                .getNrqlCondition();
    }
}
