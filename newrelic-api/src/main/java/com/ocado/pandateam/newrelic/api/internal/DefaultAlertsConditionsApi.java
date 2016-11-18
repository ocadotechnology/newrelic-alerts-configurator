package com.ocado.pandateam.newrelic.api.internal;

import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.RequestBodyEntity;
import com.ocado.pandateam.newrelic.api.AlertsConditionsApi;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.conditions.AlertsCondition;
import com.ocado.pandateam.newrelic.api.model.conditions.AlertsConditionList;
import com.ocado.pandateam.newrelic.api.model.conditions.AlertsConditionWrapper;

import java.util.List;

public class DefaultAlertsConditionsApi extends ApiBase implements AlertsConditionsApi {

    private static final String CONDITIONS_URL = "/v2/alerts_conditions";
    private static final String CONDITION_URL = "/v2/alerts_conditions/{condition_id}.json";
    private static final String CONDITION_POLICY_URL = "/v2/alerts_conditions/policies/{policy_id}.json";

    DefaultAlertsConditionsApi(NewRelicRestClient api) {
        super(api);
    }

    @Override
    public List<AlertsCondition> list(int policyId) throws NewRelicApiException {
        HttpRequest request = api.get(CONDITIONS_URL).queryString("policy_id", policyId);
        return api.asObject(request, AlertsConditionList.class).getList();
    }

    @Override
    public AlertsCondition create(int policyId, AlertsCondition condition) throws NewRelicApiException {
        RequestBodyEntity request = api.post(CONDITION_POLICY_URL)
                .routeParam("policy_id", String.valueOf(policyId))
                .body(new AlertsConditionWrapper(condition));
        return api.asObject(request, AlertsConditionWrapper.class).getCondition();
    }

    @Override
    public AlertsCondition update(int conditionId, AlertsCondition condition) throws NewRelicApiException {
        RequestBodyEntity request = api.put(CONDITION_URL)
                .routeParam("condition_id", String.valueOf(conditionId))
                .body(new AlertsConditionWrapper(condition));
        return api.asObject(request, AlertsConditionWrapper.class).getCondition();
    }

    @Override
    public AlertsCondition delete(int conditionId) throws NewRelicApiException {
        HttpRequest request = api.delete(CONDITION_URL).routeParam("condition_id", String.valueOf(conditionId));
        return api.asObject(request, AlertsConditionWrapper.class).getCondition();
    }
}
