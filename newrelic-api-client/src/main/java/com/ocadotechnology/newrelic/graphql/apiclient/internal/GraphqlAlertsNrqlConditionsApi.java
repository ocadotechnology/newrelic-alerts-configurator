package com.ocadotechnology.newrelic.graphql.apiclient.internal;

import com.github.k0kubun.builder.query.graphql.builder.ObjectBuilder;
import com.ocadotechnology.newrelic.apiclient.PolicyItemApi;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.AlertsNrqlCondition;
import com.ocadotechnology.newrelic.graphql.apiclient.GraphqlApiError;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.NrqlCondition;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.NrqlConditionsSearch;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.PageIterator;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.github.k0kubun.builder.query.graphql.GraphQLQueryBuilder.query;
import static com.ocadotechnology.newrelic.graphql.apiclient.internal.util.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class GraphqlAlertsNrqlConditionsApi extends ApiBase implements PolicyItemApi<AlertsNrqlCondition> {
    private final long accountId;
    private final ModelTranslator modelTranslator;

    public GraphqlAlertsNrqlConditionsApi(QueryExecutor executor, long accountId) {
        super(executor);
        this.accountId = accountId;
        this.modelTranslator = new ModelTranslator();
    }

    public GraphqlAlertsNrqlConditionsApi(QueryExecutor executor, long accountId, ModelTranslator modelTranslator) {
        super(executor);
        this.accountId = accountId;
        this.modelTranslator = modelTranslator;
    }

    @Override
    public List<AlertsNrqlCondition> list(int policyId) {
        return doList(policyId)
                .map(modelTranslator::toModel)
                .collect(toList());
    }

    private Stream<NrqlCondition> doList(int policyId) {
        return asStream(new PageIterator<>(cursor -> fetchListPage(policyId, cursor)))
                .flatMap(Collection::stream);
    }

    private Optional<NrqlConditionsSearch> fetchListPage(int policyId, String cursor) {
        String query = query()
                .object("actor", object(actor -> actor
                        .object("account", param("id", accountId), object(account -> account
                                .object("alerts", object(alerts -> alerts
                                        .object("nrqlConditionsSearch", params(
                                                        "cursor", cursor, "searchCriteria",
                                                        complexParamValue(param("policyId", policyId))),
                                                object(notificationChannels -> notificationChannels
                                                        .field("nextCursor")
                                                        .object("nrqlConditions",
                                                                object(nrqlCondition()))
                                                ))))))
                )).build();

        return throwOnError(execute(Query.wrapped(query))).getNrqlConditionsSearch();
    }

    private UnaryOperator<ObjectBuilder> nrqlCondition() {
        return nrqlCondition -> nrqlCondition
                .field("id")
                .field("name")
                .field("enabled")
                .field("runbookUrl")
                .object("nrql",
                        object(nrql -> nrql.field("query")))
                .object("signal",
                        object(signal -> signal
                                .field("aggregationDelay")
                                .field("aggregationMethod")
                                .field("aggregationTimer")
                                .field("aggregationWindow")
                                .field("evaluationDelay")
                                .field("fillOption")
                                .field("fillValue")
                                .field("slideBy")))
                .object("terms",
                        object(terms -> terms
                                .field("operator")
                                .field("priority")
                                .field("threshold")
                                .field("thresholdDuration")
                                .field("thresholdOccurrences")))
                .object("expiration", object(expiration -> expiration
                        .field("closeViolationsOnExpiration")
                        .field("expirationDuration")
                        .field("openViolationOnExpiration")
                ))
                .on("AlertsNrqlStaticCondition",
                        object(staticCondition -> staticCondition
                                .field("valueFunction")));
    }

    @Override
    public AlertsNrqlCondition create(int policyId, AlertsNrqlCondition condition) {
        checkArgument(nonNull(condition), "condition must not be null");
        checkArgument(nonNull(condition.getName()), "name must not be null");

        String query = query()
                .object("mutation",
                        object(mutation -> mutation.object(
                                "alertsNrqlConditionStaticCreate",
                                params(
                                        "accountId", accountId,
                                        "policyId", policyId,
                                        "condition", complexParamValue(modelTranslator.toNrqlCondition(condition))),
                                object(nrqlCondition())))).build();

        return throwOnError(execute(Query.of(query))).getAlertsNrqlConditionStaticCreate().map(modelTranslator::toModel).get();
    }

    @Override
    public AlertsNrqlCondition update(int conditionId, AlertsNrqlCondition condition) {
        checkArgument(nonNull(condition), "condition must not be null");

        String query = query()
                .object("mutation",
                        object(mutation -> mutation.object(
                                "alertsNrqlConditionStaticUpdate",
                                params(
                                        "accountId", accountId,
                                        "id", conditionId,
                                        "condition", complexParamValue(modelTranslator.toNrqlCondition(condition))),
                                object(nrqlCondition())))).build();

        return throwOnError(execute(Query.of(query))).getAlertsNrqlConditionStaticUpdate().map(modelTranslator::toModel).get();
    }

    @Override
    public AlertsNrqlCondition delete(int policyId, int conditionId) {
        AlertsNrqlCondition condition = getCondition(policyId, conditionId)
                .orElseThrow(() -> new GraphqlApiError(String.format("Condition %d not found in policy %d", conditionId, policyId)));

        String query = query()
                .object("mutation",
                        object(mutation -> mutation.object(
                                "alertsConditionDelete",
                                params(
                                        "accountId", accountId,
                                        "id", conditionId),
                                object(alertsConditionDelete -> alertsConditionDelete.field("id"))))).build();

        throwOnError(execute(Query.of(query)));

        return condition;
    }

    private Optional<AlertsNrqlCondition> getCondition(int policyId, int conditionId) {
        return doList(policyId).filter(condition -> condition.getId().equals("" + conditionId))
                .findFirst()
                .map(modelTranslator::toModel);
    }
}
