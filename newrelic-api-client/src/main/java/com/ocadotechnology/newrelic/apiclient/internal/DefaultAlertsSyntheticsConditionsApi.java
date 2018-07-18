package com.ocadotechnology.newrelic.apiclient.internal;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

import java.util.List;

import javax.ws.rs.client.Entity;

import com.ocadotechnology.newrelic.apiclient.PolicyItemApi;
import com.ocadotechnology.newrelic.apiclient.internal.client.NewRelicClient;
import com.ocadotechnology.newrelic.apiclient.internal.model.AlertsSyntheticsConditionList;
import com.ocadotechnology.newrelic.apiclient.internal.model.AlertsSyntheticsConditionWrapper;
import com.ocadotechnology.newrelic.apiclient.model.conditions.synthetics.AlertsSyntheticsCondition;

class DefaultAlertsSyntheticsConditionsApi extends ApiBase implements PolicyItemApi<AlertsSyntheticsCondition> {

    private static final String CONDITIONS_URL = "/v2/alerts_synthetics_conditions";
    private static final String CONDITION_URL = "/v2/alerts_synthetics_conditions/{condition_id}.json";
    private static final String CONDITION_POLICY_URL = "/v2/alerts_synthetics_conditions/policies/{policy_id}.json";

    DefaultAlertsSyntheticsConditionsApi(NewRelicClient client) {
        super(client);
    }

    @Override
    public List<AlertsSyntheticsCondition> list(int policyId) {
        return getPageable(
                client.target(CONDITIONS_URL).queryParam("policy_id", policyId).request(APPLICATION_JSON_TYPE),
                AlertsSyntheticsConditionList.class)
                .getList();
    }

    @Override
    public AlertsSyntheticsCondition create(int policyId, AlertsSyntheticsCondition syntheticsCondition) {
        return client
                .target(CONDITION_POLICY_URL)
                .resolveTemplate("policy_id", policyId)
                .request(APPLICATION_JSON_TYPE)
                .post(Entity.entity(new AlertsSyntheticsConditionWrapper(syntheticsCondition), APPLICATION_JSON_TYPE),
                        AlertsSyntheticsConditionWrapper.class)
                .getSyntheticsCondition();
    }

    @Override
    public AlertsSyntheticsCondition update(int conditionId, AlertsSyntheticsCondition syntheticsCondition) {
        return client
                .target(CONDITION_URL)
                .resolveTemplate("condition_id", conditionId)
                .request(APPLICATION_JSON_TYPE)
                .put(Entity.entity(new AlertsSyntheticsConditionWrapper(syntheticsCondition), APPLICATION_JSON_TYPE),
                        AlertsSyntheticsConditionWrapper.class)
                .getSyntheticsCondition();
    }

    @Override
    public AlertsSyntheticsCondition delete(int conditionId) {
        return client
                .target(CONDITION_URL)
                .resolveTemplate("condition_id", conditionId)
                .request(APPLICATION_JSON_TYPE)
                .delete(AlertsSyntheticsConditionWrapper.class)
                .getSyntheticsCondition();
    }
}
