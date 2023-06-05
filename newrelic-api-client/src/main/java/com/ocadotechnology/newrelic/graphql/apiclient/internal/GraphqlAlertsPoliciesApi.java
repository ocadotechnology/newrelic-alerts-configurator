package com.ocadotechnology.newrelic.graphql.apiclient.internal;

import com.ocadotechnology.newrelic.apiclient.AlertsPoliciesApi;
import com.ocadotechnology.newrelic.apiclient.model.policies.AlertsPolicy;
import com.ocadotechnology.newrelic.apiclient.model.policies.AlertsPolicyChannels;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.JavaLikeEnum;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Query;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.k0kubun.builder.query.graphql.GraphQLQueryBuilder.query;
import static com.ocadotechnology.newrelic.graphql.apiclient.internal.util.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

public class GraphqlAlertsPoliciesApi extends ApiBase implements AlertsPoliciesApi {
    private final long accountId;

    public GraphqlAlertsPoliciesApi(QueryExecutor executor, long accountId) {
        super(executor);
        this.accountId = accountId;
    }

    @Override
    public Optional<AlertsPolicy> getByName(String alertsPolicyName) {
        String query = query()
                .object("actor",
                        object(actor -> actor.object("account", param("id", accountId),
                                object(account -> account.object("alerts",
                                        object(alerts -> alerts.object(
                                                "policiesSearch",
                                                param("searchCriteria", complexParamValue(param("name", alertsPolicyName))),
                                                object(policiesSearch -> policiesSearch.object("policies", object(policies -> policies
                                                        .field("id")
                                                        .field("name")
                                                        .field("incidentPreference"))))))))))).build();

        List<AlertsPolicy> policies = execute(Query.wrapped(query)).getPolicies();
        return policies.isEmpty() ? Optional.empty() : Optional.ofNullable(policies.get(0));
    }

    public Optional<AlertsPolicy> getById(int policyId) {
        return getPolicy(doGetById(policyId));
    }

    private Result doGetById(int policyId) {
        String query = query()
                .object("actor",
                        object(actor -> actor.object("account", param("id", accountId),
                                object(account -> account.object("alerts",
                                        object(alerts -> alerts.object("policy", param("id", policyId),
                                                object(policy -> policy
                                                        .field("id")
                                                        .field("name")
                                                        .field("incidentPreference"))))))))).build();

        return execute(Query.wrapped(query));
    }

    private Optional<AlertsPolicy> getPolicy(Result result) {
        return result.notFound() ? Optional.empty() : throwOnError(result).getAlertsPolicy();
    }

    @Override
    public AlertsPolicy create(AlertsPolicy policy) {
        checkArgument(nonNull(policy), "policy must not be null");
        checkArgument(nonNull(policy.getName()), "name must not be null");
        checkArgument(nonNull(policy.getIncidentPreference()), "incident preference must not be null");

        String query = query()
                .object("mutation",
                        object(mutation -> mutation.object(
                                "alertsPolicyCreate",
                                params(
                                        "accountId", accountId,
                                        "policy", complexParamValue(params(
                                                "name", policy.getName(),
                                                "incidentPreference", new JavaLikeEnum(policy.getIncidentPreference())))),

                                object(alertsPolicyCreate -> alertsPolicyCreate
                                        .field("id")
                                        .field("name")
                                        .field("incidentPreference"))))).build();

        return throwOnError(execute(Query.of(query))).getAlertsPolicyCreate().get();
    }

    @Override
    public AlertsPolicy delete(int policyId) {
        AlertsPolicy policy = throwOnError(doGetById(policyId)).getAlertsPolicy().get();

        String query = query()
                .object("mutation",
                        object(mutation -> mutation.object(
                                "alertsPolicyDelete",
                                params(
                                        "accountId", accountId,
                                        "id", "" + policyId),
                                object(alertsPolicyDelete -> alertsPolicyDelete.field("id")))))
                .build();

        throwOnError(execute(Query.of(query)));
        return policy;
    }

    @Override
    public AlertsPolicyChannels updateChannels(AlertsPolicyChannels channels) {
        checkArgument(nonNull(channels), "channels must not be null");
        checkArgument(nonNull(channels.getPolicyId()), "policy id must not be null");

        String query = query()
                .object("mutation",
                        object(mutation -> mutation.object(
                                "alertsNotificationChannelsAddToPolicy",
                                params(
                                        "accountId", accountId,
                                        "policyId", channels.getPolicyId(),
                                        "notificationChannelIds", Optional.ofNullable(channels.getChannelIds()).orElseGet(ArrayList::new)),
                                object(alertsNotificationChannelsAddToPolicy -> alertsNotificationChannelsAddToPolicy
                                        .object("notificationChannels", object(notificationChannels -> notificationChannels
                                                .field("id")))
                                        .object("errors", object(errors -> errors
                                                .field("description")
                                                .field("errorType")
                                                .field("notificationChannelId")))))))
                .build();

        List<Integer> notificationChannelIds = throwOnError(execute(Query.of(query)), Result::getAlertsNotificationChannelsAddToPolicy)
                .orElseGet(ArrayList::new);
        return AlertsPolicyChannels.builder().policyId(channels.getPolicyId()).channelIds(notificationChannelIds).build();
    }
}
