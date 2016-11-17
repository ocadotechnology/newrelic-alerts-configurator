package com.ocado.pandateam.newrelic.api;

import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.MultipartBody;
import com.mashape.unirest.request.body.RequestBodyEntity;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.NewRelicRestClient;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicyChannels;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicyChannelsWrapper;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicyList;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicyWrapper;

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
     * Gets {@link AlertsPolicy} object using its name.
     *
     * @param name name of the alert policy registered in NewRelic
     * @return Optional containing {@link AlertsPolicy} object, or empty if alert policy not found
     * @throws NewRelicApiException when more than one response returned or received error response
     */
    public Optional<AlertsPolicy> getByName(String name) throws NewRelicApiException {
        HttpRequest request = api.get(POLICIES_URL).queryString("filter[name]", name);
        return api.asObject(request, AlertsPolicyList.class).getSingle();
    }

    /**
     * Creates Alerts Policy instance.
     *
     * @param policy - policy definition to be created
     * @return created {@link AlertsPolicy}
     * @throws NewRelicApiException when received error response
     */
    public AlertsPolicy create(AlertsPolicy policy) throws NewRelicApiException {
        RequestBodyEntity request = api.post(POLICIES_URL).body(new AlertsPolicyWrapper(policy));
        return api.asObject(request, AlertsPolicyWrapper.class).getPolicy();
    }

    /**
     * Deletes Alerts Policy instance.
     *
     * @param policyId - id of the policy to be removed
     * @return deleted {@link AlertsPolicy}
     * @throws NewRelicApiException when received error response
     */
    public AlertsPolicy delete(int policyId) throws NewRelicApiException {
        HttpRequest request = api.delete(POLICY_URL).routeParam("policy_id", String.valueOf(policyId));
        return api.asObject(request, AlertsPolicyWrapper.class).getPolicy();
    }

    /**
     * Associates given channels to the policy. This method does not remove previously linked channel but not provided in the list.
     *
     * @param channels - {@link AlertsPolicyChannels} object mapping policy id to the list of channels ids
     * @return {@link AlertsPolicyChannels} when successfully set
     * @throws NewRelicApiException when received error response
     */
    public AlertsPolicyChannels updateChannels(AlertsPolicyChannels channels) throws NewRelicApiException {
        MultipartBody request = api.put(POLICY_CHANNELS_URL, "application/x-www-form-urlencoded")
                .field("policy_id", channels.getPolicyId())
                .field("channel_ids",
                        channels.getChannelIds()
                                .stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(",")));
        return api.asObject(request, AlertsPolicyChannelsWrapper.class).getPolicyChannels();
    }
}
