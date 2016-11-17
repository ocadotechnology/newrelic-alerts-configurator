package com.ocado.pandateam.newrelic.api;

import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.RequestBodyEntity;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.NewRelicRestClient;
import com.ocado.pandateam.newrelic.api.model.conditions.AlertsCondition;
import com.ocado.pandateam.newrelic.api.model.conditions.AlertsConditionList;
import com.ocado.pandateam.newrelic.api.model.conditions.AlertsConditionWrapper;

import java.util.List;

public class AlertsConditionsApi extends BaseApi {

    private static final String CONDITIONS_URL = "/v2/alerts_conditions";
    private static final String CONDITION_URL = "/v2/alerts_conditions/{condition_id}.json";
    private static final String CONDITION_POLICY_URL = "/v2/alerts_conditions/policies/{policy_id}.json";

    AlertsConditionsApi(NewRelicRestClient api) {
        super(api);
    }

    /**
     * Lists Alerts Conditions for the given policy.
     *
     * @param policyId - id of the policy containing alerts conditions
     * @return list of all existing {@link AlertsCondition} from the given policy
     * @throws NewRelicApiException when received error response
     */
    public List<AlertsCondition> list(int policyId) throws NewRelicApiException {
        HttpRequest request = api.get(CONDITIONS_URL).queryString("policy_id", policyId);
        return api.asObject(request, AlertsConditionList.class).getList();
    }

    /**
     * Creates Alerts Condition instance within specified policy.
     *
     * @param policyId  - id of the policy to be updated
     * @param condition - condition definition to be created
     * @return created {@link AlertsCondition}
     * @throws NewRelicApiException when received error response
     */
    public AlertsCondition create(int policyId, AlertsCondition condition) throws NewRelicApiException {
        RequestBodyEntity request = api.post(CONDITION_POLICY_URL)
                .routeParam("policy_id", String.valueOf(policyId))
                .body(new AlertsConditionWrapper(condition));
        return api.asObject(request, AlertsConditionWrapper.class).getCondition();
    }

    /**
     * Updates Alerts Condition definition.
     *
     * @param conditionId - id of the condition to be updated
     * @param condition   - condition definition to be updated
     * @return created {@link AlertsCondition}
     * @throws NewRelicApiException when received error response
     */
    public AlertsCondition update(int conditionId, AlertsCondition condition) throws NewRelicApiException {
        RequestBodyEntity request = api.put(CONDITION_URL)
                .routeParam("condition_id", String.valueOf(conditionId))
                .body(new AlertsConditionWrapper(condition));
        return api.asObject(request, AlertsConditionWrapper.class).getCondition();
    }

    /**
     * Deletes Alerts Condition.
     *
     * @param conditionId - id of the condition to be updated
     * @return deleted {@link AlertsCondition}
     * @throws NewRelicApiException when received error response
     */
    public AlertsCondition delete(int conditionId) throws NewRelicApiException {
        HttpRequest request = api.delete(CONDITION_URL).routeParam("condition_id", String.valueOf(conditionId));
        return api.asObject(request, AlertsConditionWrapper.class).getCondition();
    }
}
