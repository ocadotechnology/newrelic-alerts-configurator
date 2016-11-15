package com.ocado.pandateam.newrelic.api;

import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.RequestBodyEntity;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.NewRelicRestClient;
import com.ocado.pandateam.newrelic.api.model.conditions.AlertCondition;
import com.ocado.pandateam.newrelic.api.model.conditions.AlertConditionList;
import com.ocado.pandateam.newrelic.api.model.conditions.AlertConditionWrapper;

public class AlertsConditionsApi extends BaseApi {

    private static final String CONDITIONS_URL = "/v2/alerts_conditions";
    private static final String CONDITION_URL = "/v2/alerts_conditions/{condition_id}.json";
    private static final String CONDITION_POLICY_URL = "/v2/alerts_conditions/policies/{policy_id}.json";

    AlertsConditionsApi(NewRelicRestClient api) {
        super(api);
    }

    public AlertConditionList list(int policyId) throws NewRelicApiException {
        HttpRequest request = api.get(CONDITIONS_URL).queryString("policy_id", policyId);
        return api.asObject(request, AlertConditionList.class);
    }

    public AlertCondition create(int policyId, AlertCondition condition)
            throws NewRelicApiException {
        RequestBodyEntity request = api.post(CONDITION_POLICY_URL)
                .routeParam("policy_id", String.valueOf(policyId))
                .body(new AlertConditionWrapper(condition));
        return api.asObject(request, AlertConditionWrapper.class).getCondition();
    }

    public AlertCondition update(int conditionId, AlertCondition condition)
            throws NewRelicApiException {
        RequestBodyEntity request = api.put(CONDITION_URL)
                .routeParam("condition_id", String.valueOf(conditionId))
                .body(new AlertConditionWrapper(condition));
        return api.asObject(request, AlertConditionWrapper.class).getCondition();
    }

    public AlertCondition delete(int conditionId) throws NewRelicApiException {
        HttpRequest request = api.delete(CONDITION_URL).routeParam("condition_id", String.valueOf(conditionId));
        return api.asObject(request, AlertConditionWrapper.class).getCondition();
    }
}
