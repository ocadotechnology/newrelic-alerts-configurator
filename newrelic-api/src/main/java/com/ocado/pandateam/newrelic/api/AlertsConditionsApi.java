package com.ocado.pandateam.newrelic.api;

import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.RequestBodyEntity;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.NewRelicRestClient;
import com.ocado.pandateam.newrelic.api.model.AlertCondition;
import com.ocado.pandateam.newrelic.api.model.AlertConditionList;
import com.ocado.pandateam.newrelic.api.model.AlertConditionWrapper;

public class AlertsConditionsApi {

    private static final String CONDITIONS_URL = "/v2/alerts_conditions";
    private static final String CONDITION_URL = "/v2/alerts_conditions/{condition_id}.json";
    private static final String CONDITION_POLICY_URL = "/v2/alerts_conditions/policies/{policy_id}.json";

    private final NewRelicRestClient api;

    public AlertsConditionsApi(NewRelicRestClient api) {
        this.api = api;
    }

    public AlertConditionList list(int policyId) throws NewRelicApiException {
        HttpRequest request = api.get(CONDITIONS_URL).queryString("policy_id", policyId);
        return api.asObject(request, AlertConditionList.class);
    }

    public AlertCondition create(int policyId, AlertCondition AlertCondition)
            throws NewRelicApiException {
        RequestBodyEntity request = api.post(CONDITION_POLICY_URL)
                .routeParam("policy_id", String.valueOf(policyId))
                .body(new AlertConditionWrapper(AlertCondition));
        return api.asObject(request, AlertConditionWrapper.class).getCondition();
    }

    public AlertCondition update(int conditionId, AlertCondition AlertCondition)
            throws NewRelicApiException {
        RequestBodyEntity request = api.put(CONDITION_URL)
                .routeParam("condition_id", String.valueOf(conditionId))
                .body(new AlertConditionWrapper(AlertCondition));
        return api.asObject(request, AlertConditionWrapper.class).getCondition();
    }

    public AlertCondition delete(int conditionId) throws NewRelicApiException {
        HttpRequest request = api.delete(CONDITION_URL).routeParam("condition_id", String.valueOf(conditionId));
        return api.asObject(request, AlertConditionWrapper.class).getCondition();
    }
}
