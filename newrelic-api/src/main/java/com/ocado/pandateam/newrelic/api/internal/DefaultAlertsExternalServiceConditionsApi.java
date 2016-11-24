package com.ocado.pandateam.newrelic.api.internal;

import com.ocado.pandateam.newrelic.api.AlertsExternalServiceConditionsApi;
import com.ocado.pandateam.newrelic.api.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.client.NewRelicClient;
import com.ocado.pandateam.newrelic.api.internal.model.AlertsExternalServiceConditionList;
import com.ocado.pandateam.newrelic.api.internal.model.AlertsExternalServiceConditionWrapper;
import com.ocado.pandateam.newrelic.api.model.ObjectList;
import com.ocado.pandateam.newrelic.api.model.conditions.external.AlertsExternalServiceCondition;

import javax.ws.rs.client.Entity;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

class DefaultAlertsExternalServiceConditionsApi extends ApiBase implements AlertsExternalServiceConditionsApi {

    private static final String CONDITIONS_URL = "/v2/alerts_external_service_conditions";
    private static final String CONDITION_URL = "/v2/alerts_external_service_conditions/{condition_id}.json";
    private static final String CONDITION_POLICY_URL = "/v2/alerts_external_service_conditions/policies/{policy_id}.json";

    DefaultAlertsExternalServiceConditionsApi(NewRelicClient client) {
        super(client);
    }

    @Override
    public List<AlertsExternalServiceCondition> list(int policyId) throws NewRelicApiException {
        return getPageable(
                client.target(CONDITIONS_URL).queryParam("policy_id", policyId).request(APPLICATION_JSON_TYPE),
                AlertsExternalServiceConditionList.class,
                (list1, list2) -> new AlertsExternalServiceConditionList(ObjectList.merge(list1, list2))).getList();
    }

    @Override
    public AlertsExternalServiceCondition create(int policyId, AlertsExternalServiceCondition condition)
            throws NewRelicApiException {
        return client
                .target(CONDITION_POLICY_URL)
                .resolveTemplate("policy_id", policyId)
                .request(APPLICATION_JSON_TYPE)
                .post(Entity.entity(new AlertsExternalServiceConditionWrapper(condition), APPLICATION_JSON_TYPE),
                        AlertsExternalServiceConditionWrapper.class)
                .getExternalServiceCondition();
    }

    @Override
    public AlertsExternalServiceCondition update(int conditionId, AlertsExternalServiceCondition condition)
            throws NewRelicApiException {
        return client
                .target(CONDITION_URL)
                .resolveTemplate("condition_id", conditionId)
                .request(APPLICATION_JSON_TYPE)
                .put(Entity.entity(new AlertsExternalServiceConditionWrapper(condition), APPLICATION_JSON_TYPE),
                        AlertsExternalServiceConditionWrapper.class)
                .getExternalServiceCondition();
    }

    @Override
    public AlertsExternalServiceCondition delete(int conditionId) throws NewRelicApiException {
        return client
                .target(CONDITION_URL)
                .resolveTemplate("condition_id", conditionId)
                .request(APPLICATION_JSON_TYPE)
                .delete(AlertsExternalServiceConditionWrapper.class)
                .getExternalServiceCondition();
    }
}
