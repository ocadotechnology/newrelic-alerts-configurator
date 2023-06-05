package com.ocadotechnology.newrelic.graphql.apiclient.internal;

import com.github.k0kubun.builder.query.graphql.builder.ObjectBuilder;
import com.github.k0kubun.builder.query.graphql.model.GraphQLObject;
import com.ocadotechnology.newrelic.apiclient.AlertsChannelsApi;
import com.ocadotechnology.newrelic.apiclient.model.channels.AlertsChannel;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.NotificationChannels;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.PageIterator;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Query;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Result;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.k0kubun.builder.query.graphql.GraphQLQueryBuilder.query;
import static java.util.stream.Collectors.toList;

public class GraphqlAlertsChannelsApi extends ApiBase implements AlertsChannelsApi {
    private final long accountId;
    private final ModelTranslator modelTranslator;

    public GraphqlAlertsChannelsApi(QueryExecutor executor, long accountId) {
        super(executor);
        this.accountId = accountId;
        this.modelTranslator = new ModelTranslator();
    }

    public GraphqlAlertsChannelsApi(QueryExecutor executor, long accountId, ModelTranslator modelTranslator) {
        super(executor);
        this.accountId = accountId;
        this.modelTranslator = modelTranslator;
    }

    @Override
    public List<AlertsChannel> list() {
        return asStream(new PageIterator<>(this::fetchListPage))
                .flatMap(Collection::stream)
                .map(modelTranslator::toModel)
                .collect(toList());
    }

    private Optional<NotificationChannels> fetchListPage(String cursor) {
        String query = query()
                .object("actor", object(actor -> actor
                        .object("account", param("id", accountId), object(account -> account
                                .object("alerts", object(alerts -> alerts
                                        .object("notificationChannels", param("cursor", cursor),
                                                object(notificationChannels -> notificationChannels
                                                        .field("nextCursor")
                                                        .object("channels", notificationChannelSchema())
                                                )))))))).build();

        return throwOnError(execute(Query.wrapped(query))).getNotificationChannels();
    }

    @Override
    @SneakyThrows
    public AlertsChannel create(AlertsChannel channel) {
        Map<String, Object> channelConfig = modelTranslator.toChannelConfig(channel.getType(), channel.getName(), channel.getConfiguration());
        String query = query()
                .object("mutation",
                        object(mutation -> mutation.object(
                                "alertsNotificationChannelCreate",
                                params(
                                        "accountId", accountId,
                                        "notificationChannel", complexParamValue(channelConfig)),
                                object(alertsNotificationChannelCreate -> alertsNotificationChannelCreate
                                        .object("error", object(errors -> errors
                                                .field("description")
                                                .field("errorType")
                                        ))
                                        .object("notificationChannel",
                                                object(notificationChannel -> notificationChannel
                                                        .on("AlertsEmailNotificationChannel",
                                                                object(ch -> channelSpec(ch, emailConfig())))
                                                        .on("AlertsPagerDutyNotificationChannel",
                                                                object(ch -> channelSpec(ch, pagerDutyConfig())))
                                                        .on("AlertsSlackNotificationChannel",
                                                                object(ch -> channelSpec(ch, slackConfig())))
                                                        .on("AlertsWebhookNotificationChannel",
                                                                object(ch -> channelSpec(ch, webhookConfig())))))))))
                .build();

        return throwOnError(execute(Query.of(query)), Result::getAlertsNotificationChannelCreate).map(modelTranslator::toModel).get();
    }

    private ObjectBuilder channelSpec(ObjectBuilder channel, GraphQLObject config) {
        return channel
                .field("id")
                .field("name")
                .field("type")
                .object("config", config)
                .object("associatedPolicies", object(associatedPolicies -> associatedPolicies
                        .object("policies", object(policies -> policies
                                .field("id")))));
    }

    @Override
    public AlertsChannel delete(int channelId) {
        AlertsChannel channel = throwOnError(doGetById(channelId)).getNotificationChannel()
                .map(modelTranslator::toModel).get();

        String query = query()
                .object("mutation",
                        object(mutation -> mutation.object(
                                "alertsNotificationChannelDelete",
                                params("accountId", accountId, "id", "" + channelId),
                                object(alertsNotificationChannelDelete -> alertsNotificationChannelDelete
                                        .object("error", object(error -> error
                                                .field("description")
                                                .field("errorType")
                                                .field("notificationChannelId")))))))
                .build();

        throwOnError(execute(Query.of(query)), Result::getAlertsNotificationChannelDelete);
        return channel;
    }

    @Override
    public AlertsChannel deleteFromPolicy(int policyId, int channelId) {
        AlertsChannel channel = throwOnError(doGetById(channelId)).getNotificationChannel()
                .map(modelTranslator::toModel).get();

        String query = query()
                .object("mutation",
                        object(mutation -> mutation.object(
                                "alertsNotificationChannelsRemoveFromPolicy",
                                params(
                                        "accountId", accountId,
                                        "policyId", "" + policyId,
                                        "notificationChannelIds", "" + channelId),
                                object(alertsNotificationChannelsRemoveFromPolicy -> alertsNotificationChannelsRemoveFromPolicy
                                        .object("notificationChannels", object(notificationChannels -> notificationChannels
                                                .field("id")))
                                        .object("errors", object(errors -> errors
                                                .field("description")
                                                .field("errorType")
                                                .field("notificationChannelId")))))))
                .build();

        throwOnError(execute(Query.of(query)), Result::getAlertsNotificationChannelsRemoveFromPolicy);
        return channel;
    }

    public Optional<AlertsChannel> getById(int channelId) {
        return getChannel(doGetById(channelId));
    }

    private Result doGetById(int channelId) {
        String query = query()
                .object("actor", object(actor -> actor
                        .object("account", param("id", accountId), object(account -> account
                                .object("alerts", object(alerts -> alerts
                                        .object("notificationChannel", param("id", "" + channelId), notificationChannelSchema()))))))).build();
        return execute(Query.wrapped(query));
    }

    private GraphQLObject notificationChannelSchema() {
        return object(notificationChannel -> notificationChannel
                .field("id")
                .field("name")
                .field("type")
                .object("associatedPolicies", object(associatedPolicies -> associatedPolicies.object("policies", object(policies -> policies
                        .field("id")))))
                .on("AlertsEmailNotificationChannel", object(channel -> channel
                        .object("config", emailConfig())))
                .on("AlertsSlackNotificationChannel", object(channel -> channel
                        .object("config", slackConfig())))
                .on("AlertsPagerDutyNotificationChannel", object(channel -> channel
                        .object("config", pagerDutyConfig())))
                .on("AlertsWebhookNotificationChannel", object(channel -> channel
                        .object("config", webhookConfig())))
                .on("AlertsUserNotificationChannel", object(channel -> channel
                        .object("config", object(config -> config
                                .field("userId")))))
        );
    }

    private Optional<AlertsChannel> getChannel(Result result) {
        return result.notFound()
                ? Optional.empty()
                : throwOnError(result).getNotificationChannel().map(modelTranslator::toModel);
    }

    private GraphQLObject emailConfig() {
        return object(config -> config
                .field("emails")
                .field("includeJson"));
    }

    private GraphQLObject slackConfig() {
        return object(config -> config
                .field("teamChannel")
                .field("url"));
    }

    private GraphQLObject pagerDutyConfig() {
        return object(config -> config
                .field("apiKey"));
    }

    private GraphQLObject webhookConfig() {
        return object(config -> config
                .field("baseUrl")
                .object("basicAuth", object(basicAuth -> basicAuth
                        .field("username")
                        .field("password")))
                .object("customHttpHeaders", object(headers -> headers
                        .field("name")
                        .field("value")))
                .field("customPayloadBody")
                .field("customPayloadType"));
    }
}
