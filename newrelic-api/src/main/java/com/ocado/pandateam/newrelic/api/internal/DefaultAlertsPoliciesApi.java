package com.ocado.pandateam.newrelic.api.internal;

import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.MultipartBody;
import com.mashape.unirest.request.body.RequestBodyEntity;
import com.ocado.pandateam.newrelic.api.AlertsPoliciesApi;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.model.AlertsPolicyChannelsWrapper;
import com.ocado.pandateam.newrelic.api.internal.model.AlertsPolicyList;
import com.ocado.pandateam.newrelic.api.internal.model.AlertsPolicyWrapper;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicyChannels;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.ocado.pandateam.newrelic.api.internal.NewRelicRequestConstants.APPLICATION_X_WWW_FORM_URLENCODED;

public class DefaultAlertsPoliciesApi extends ApiBase implements AlertsPoliciesApi {

    private static final String POLICIES_URL = "/v2/alerts_policies.json";
    private static final String POLICY_URL = "/v2/alerts_policies/{policy_id}.json";
    private static final String POLICY_CHANNELS_URL = "/v2/alerts_policy_channels.json";

    DefaultAlertsPoliciesApi(NewRelicRestClient api) {
        super(api);
    }

    @Override
    public Optional<AlertsPolicy> getByName(String name) throws NewRelicApiException {
        HttpRequest request = api.get(POLICIES_URL).queryString("filter[name]", name);
        return api.asObject(request, AlertsPolicyList.class).getSingle();
    }

    @Override
    public AlertsPolicy create(AlertsPolicy policy) throws NewRelicApiException {
        RequestBodyEntity request = api.post(POLICIES_URL).body(new AlertsPolicyWrapper(policy));
        return api.asObject(request, AlertsPolicyWrapper.class).getPolicy();
    }

    @Override
    public AlertsPolicy delete(int policyId) throws NewRelicApiException {
        HttpRequest request = api.delete(POLICY_URL).routeParam("policy_id", String.valueOf(policyId));
        return api.asObject(request, AlertsPolicyWrapper.class).getPolicy();
    }

    @Override
    public AlertsPolicyChannels updateChannels(AlertsPolicyChannels channels) throws NewRelicApiException {
        MultipartBody request = api.put(POLICY_CHANNELS_URL, APPLICATION_X_WWW_FORM_URLENCODED)
                .field("policy_id", channels.getPolicyId())
                .field("channel_ids",
                        channels.getChannelIds()
                                .stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(",")));
        return api.asObject(request, AlertsPolicyChannelsWrapper.class).getPolicyChannels();
    }
}
