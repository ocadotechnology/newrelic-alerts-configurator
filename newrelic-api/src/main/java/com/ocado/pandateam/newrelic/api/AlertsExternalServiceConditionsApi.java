package com.ocado.pandateam.newrelic.api;

import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.RequestBodyEntity;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.NewRelicRestClient;
import com.ocado.pandateam.newrelic.api.model.conditions.external.AlertsExternalServiceCondition;
import com.ocado.pandateam.newrelic.api.model.conditions.external.AlertsExternalServiceConditionList;
import com.ocado.pandateam.newrelic.api.model.conditions.external.AlertsExternalServiceConditionWrapper;

import java.util.List;

public class AlertsExternalServiceConditionsApi extends BaseApi {

    private static final String CONDITIONS_URL = "/v2/alerts_external_service_conditions";
    private static final String CONDITION_URL = "/v2/alerts_external_service_conditions/{condition_id}.json";
    private static final String CONDITION_POLICY_URL = "/v2/alerts_external_service_conditions/policies/{policy_id}.json";

    AlertsExternalServiceConditionsApi(NewRelicRestClient api) {
        super(api);
    }

    /**
     * Lists Alerts Conditions for external services for the given policy.
     *
     * @param policyId - id of the policy containing alerts conditions
     * @return list of all existing {@link AlertsExternalServiceCondition} from the given policy
     * @throws NewRelicApiException when received error response
     */
    public List<AlertsExternalServiceCondition> list(int policyId) throws NewRelicApiException {
        HttpRequest request = api.get(CONDITIONS_URL).queryString("policy_id", policyId);
        return api.asObject(request, AlertsExternalServiceConditionList.class).getList();
    }

    /**
     * Creates Alerts Condition for external service instance within specified policy.
     *
     * @param policyId  - id of the policy to be updated
     * @param condition - condition definition to be created
     * @return created {@link AlertsExternalServiceCondition}
     * @throws NewRelicApiException when received error response
     */
    public AlertsExternalServiceCondition create(int policyId, AlertsExternalServiceCondition condition)
            throws NewRelicApiException {
        RequestBodyEntity request = api.post(CONDITION_POLICY_URL)
                .routeParam("policy_id", String.valueOf(policyId))
                .body(new AlertsExternalServiceConditionWrapper(condition));
        return api.asObject(request, AlertsExternalServiceConditionWrapper.class).getExternalServiceCondition();
    }

    /**
     * Updates Alerts Condition for external service definition.
     *
     * @param conditionId - id of the condition to be updated
     * @param condition   - condition definition to be updated
     * @return created {@link AlertsExternalServiceCondition}
     * @throws NewRelicApiException when received error response
     */
    public AlertsExternalServiceCondition update(int conditionId, AlertsExternalServiceCondition condition)
            throws NewRelicApiException {
        RequestBodyEntity request = api.put(CONDITION_URL)
                .routeParam("condition_id", String.valueOf(conditionId))
                .body(new AlertsExternalServiceConditionWrapper(condition));
        return api.asObject(request, AlertsExternalServiceConditionWrapper.class).getExternalServiceCondition();
    }

    /**
     * Deletes Alerts Condition for external service.
     *
     * @param conditionId - id of the condition to be updated
     * @return deleted {@link AlertsExternalServiceCondition}
     * @throws NewRelicApiException when received error response
     */
    public AlertsExternalServiceCondition delete(int conditionId) throws NewRelicApiException {
        HttpRequest request = api.delete(CONDITION_URL).routeParam("condition_id", String.valueOf(conditionId));
        return api.asObject(request, AlertsExternalServiceConditionWrapper.class).getExternalServiceCondition();
    }
}
