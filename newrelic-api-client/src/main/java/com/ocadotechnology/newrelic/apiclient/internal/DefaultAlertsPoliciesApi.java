package com.ocadotechnology.newrelic.apiclient.internal;

import com.ocadotechnology.newrelic.apiclient.AlertsPoliciesApi;
import com.ocadotechnology.newrelic.apiclient.internal.client.NewRelicClient;
import com.ocadotechnology.newrelic.apiclient.internal.model.AlertsPolicyChannelsWrapper;
import com.ocadotechnology.newrelic.apiclient.internal.model.AlertsPolicyList;
import com.ocadotechnology.newrelic.apiclient.internal.model.AlertsPolicyWrapper;
import com.ocadotechnology.newrelic.apiclient.model.policies.AlertsPolicy;
import com.ocadotechnology.newrelic.apiclient.model.policies.AlertsPolicyChannels;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Optional;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

class DefaultAlertsPoliciesApi extends ApiBase implements AlertsPoliciesApi {

    private static final String POLICIES_URL = "/v2/alerts_policies.json";
    private static final String POLICY_URL = "/v2/alerts_policies/{policy_id}.json";
    private static final String POLICY_CHANNELS_URL = "/v2/alerts_policy_channels.json";

    DefaultAlertsPoliciesApi(NewRelicClient client) {
        super(client);
    }

    @Override
    public Optional<AlertsPolicy> getByName(String alertsPolicyName) {
        Invocation.Builder builder = client
                .target(POLICIES_URL)
                .queryParam("filter[name]", alertsPolicyName)
                .request(APPLICATION_JSON_TYPE);
        return getPageable(builder, AlertsPolicyList.class)
                .filter(alertsPolicy -> alertsPolicy.getName().equals(alertsPolicyName))
                .getSingle();
    }

    @Override
    public AlertsPolicy create(AlertsPolicy policy) {
        return client
                .target(POLICIES_URL)
                .request(APPLICATION_JSON_TYPE)
                .post(Entity.entity(new AlertsPolicyWrapper(policy), APPLICATION_JSON_TYPE), AlertsPolicyWrapper.class)
                .getPolicy();
    }

    @Override
    public AlertsPolicy delete(int policyId) {
        return client
                .target(POLICY_URL)
                .resolveTemplate("policy_id", policyId)
                .request(APPLICATION_JSON_TYPE)
                .delete(AlertsPolicyWrapper.class)
                .getPolicy();
    }

    @Override
    public AlertsPolicyChannels updateChannels(AlertsPolicyChannels channels) {
        MultivaluedMap<String, String> form = new MultivaluedHashMap<>();
        form.putSingle("policy_id", String.valueOf(channels.getPolicyId()));
        form.putSingle("channel_ids",
                channels.getChannelIds()
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")));
        return client
                .target(POLICY_CHANNELS_URL)
                .request(APPLICATION_JSON_TYPE)
                .put(Entity.form(form), AlertsPolicyChannelsWrapper.class)
                .getPolicyChannels();
    }
}
