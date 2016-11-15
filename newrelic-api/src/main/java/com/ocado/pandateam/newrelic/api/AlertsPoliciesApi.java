package com.ocado.pandateam.newrelic.api;

import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.MultipartBody;
import com.mashape.unirest.request.body.RequestBodyEntity;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.NewRelicRestClient;
import com.ocado.pandateam.newrelic.api.model.AlertPolicy;
import com.ocado.pandateam.newrelic.api.model.AlertPolicyChannels;
import com.ocado.pandateam.newrelic.api.model.AlertPolicyChannelsWrapper;
import com.ocado.pandateam.newrelic.api.model.AlertPolicyList;
import com.ocado.pandateam.newrelic.api.model.AlertPolicyWrapper;

import java.util.Optional;
import java.util.stream.Collectors;

public class AlertsPoliciesApi extends BaseApi {

    private static final String POLICIES_URL = "/v2/alerts_policies.json";
    private static final String POLICY_URL = "/v2/alerts_policies/{policy_id}.json";
    private static final String POLICY_CHANNELS_URL = "/v2/alerts_policy_channels.json";

    AlertsPoliciesApi(NewRelicRestClient api) {
        super(api);
    }

    /**
     * Get {@link AlertPolicy} object using its name.
     *
     * @param name Name of the alert policy registered in NewRelic
     * @return Optional containing {@link AlertPolicy} object, or empty if alert policy not found
     * @throws NewRelicApiException when more than one response returned or received error response
     */
    public Optional<AlertPolicy> getByName(String name) throws NewRelicApiException {
        HttpRequest request = api.get(POLICIES_URL).queryString("filter[name]", name);
        return api.asObject(request, AlertPolicyList.class).getSingle();
    }

    /**
     * Creates {@link AlertPolicy} object using its name.
     *
     * @param name Name of the alert policy to be created
     * @return {@link AlertPolicy} object
     * @throws NewRelicApiException when received error response
     */
    public AlertPolicy create(String name) throws NewRelicApiException {
        RequestBodyEntity request = api.post(POLICIES_URL)
                .body(new AlertPolicyWrapper(AlertPolicy.builder()
                        .name(name)
                        .incidentPreference("PER_POLICY")
                        .build()));
        return api.asObject(request, AlertPolicyWrapper.class).getPolicy();
    }

    public AlertPolicy create(AlertPolicy policy) throws NewRelicApiException {
        RequestBodyEntity request = api.post(POLICIES_URL).body(new AlertPolicyWrapper(policy));
        return api.asObject(request, AlertPolicyWrapper.class).getPolicy();
    }

    public AlertPolicy delete(int policyId) throws NewRelicApiException {
        HttpRequest request = api.delete(POLICY_URL).routeParam("policy_id", String.valueOf(policyId));
        return api.asObject(request, AlertPolicyWrapper.class).getPolicy();
    }

    public AlertPolicyChannels updateChannels(AlertPolicyChannels channels) throws NewRelicApiException {
        MultipartBody request = api.put(POLICY_CHANNELS_URL, "application/x-www-form-urlencoded")
                .field("policy_id", channels.getPolicyId())
                .field("channel_ids",
                        channels.getChannelIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
        return api.asObject(request, AlertPolicyChannelsWrapper.class).getPolicyChannels();
    }
}
