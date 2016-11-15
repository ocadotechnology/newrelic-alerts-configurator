package com.ocado.pandateam.newrelic.api;

import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.RequestBodyEntity;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.NewRelicRestClient;
import com.ocado.pandateam.newrelic.api.model.ExternalServiceCondition;
import com.ocado.pandateam.newrelic.api.model.ExternalServiceConditionList;
import com.ocado.pandateam.newrelic.api.model.ExternalServiceConditionWrapper;

public class AlertsExternalServiceConditionsApi extends BaseApi {

    private static final String CONDITIONS_URL = "/v2/alerts_external_service_conditions";
    private static final String CONDITION_URL = "/v2/alerts_external_service_conditions/{condition_id}.json";
    private static final String CONDITION_POLICY_URL = "/v2/alerts_external_service_conditions/policies/{policy_id}.json";

    AlertsExternalServiceConditionsApi(NewRelicRestClient api) {
        super(api);
    }

    public ExternalServiceConditionList list(int policyId) throws NewRelicApiException {
        HttpRequest request = api.get(CONDITIONS_URL).queryString("policy_id", policyId);
        return api.asObject(request, ExternalServiceConditionList.class);
    }

    public ExternalServiceCondition create(int policyId, ExternalServiceCondition condition)
            throws NewRelicApiException {
        RequestBodyEntity request = api.post(CONDITION_POLICY_URL)
                .routeParam("policy_id", String.valueOf(policyId))
                .body(new ExternalServiceConditionWrapper(condition));
        return api.asObject(request, ExternalServiceConditionWrapper.class).getExternalServiceCondition();
    }

    public ExternalServiceCondition update(int conditionId, ExternalServiceCondition condition)
            throws NewRelicApiException {
        RequestBodyEntity request = api.put(CONDITION_URL)
                .routeParam("condition_id", String.valueOf(conditionId))
                .body(new ExternalServiceConditionWrapper(condition));
        return api.asObject(request, ExternalServiceConditionWrapper.class).getExternalServiceCondition();
    }

    public ExternalServiceCondition delete(int conditionId) throws NewRelicApiException {
        HttpRequest request = api.delete(CONDITION_URL).routeParam("condition_id", String.valueOf(conditionId));
        return api.asObject(request, ExternalServiceConditionWrapper.class).getExternalServiceCondition();
    }
}
