package com.ocadotechnology.newrelic.graphql.apiclient.internal;

import com.google.common.collect.ImmutableList;
import com.ocadotechnology.newrelic.apiclient.model.conditions.Expiration;
import com.ocadotechnology.newrelic.apiclient.model.conditions.Signal;
import com.ocadotechnology.newrelic.apiclient.model.conditions.Terms;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.AlertsNrqlCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.Nrql;
import com.ocadotechnology.newrelic.graphql.apiclient.GraphqlApiError;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.testutil.QueryExecutorMock;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Query;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Result;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.ocadotechnology.newrelic.graphql.apiclient.internal.testutil.TestResources.fromJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GraphqlAlertsNrqlConditionsApiTest {

    private static final int ACCOUNT_ID = 12345678;
    private static final int POLICY_ID = 7654321;

    private final QueryExecutorMock executor = new QueryExecutorMock();
    private GraphqlAlertsNrqlConditionsApi testee;

    @Before
    public void setUp() {
        testee = new GraphqlAlertsNrqlConditionsApi(executor, ACCOUNT_ID);
    }

    @Test
    public void list() {
        // given
        executor
                .addScenario(
                        Query.wrapped(fromJson("/AlertsNrqlConditions_list_request_part1.txt")),
                        result("/AlertsNrqlConditions_list_response_part1.json"))
                .addScenario(
                        Query.wrapped(fromJson("/AlertsNrqlConditions_list_request_part2.txt")),
                        result("/AlertsNrqlConditions_list_response_part2.json"));

        // when
        List<AlertsNrqlCondition> conditions = testee.list(POLICY_ID);

        // then
        assertThat(conditions).hasSize(2);
        assertThat(conditions).containsExactly(
                AlertsNrqlCondition.builder()
                        .id(11111111)
                        .name("Test NRQL Condition 1")
                        .nrql(new Nrql("SELECT count(*) from `test1:test-event`"))
                        .enabled(true)
                        .expiration(Expiration.builder()
                                .closeViolationsOnExpiration(true)
                                .expirationDuration("900")
                                .openViolationOnExpiration(false)
                                .build())
                        .signal(Signal.builder()
                                .aggregationDelay("120")
                                .aggregationMethod("EVENT_FLOW")
                                .aggregationTimer(null)
                                .aggregationWindow("60")
                                .fillOption("static")
                                .fillValue("0")
                                .build())
                        .terms(ImmutableList.of(
                                Terms.builder()
                                        .operator("below")
                                        .priority("critical")
                                        .threshold("1")
                                        .duration("5")
                                        .timeFunction("all")
                                        .build()
                        ))
                        .valueFunction("single_value")
                        .build(),
                AlertsNrqlCondition.builder()
                        .id(22222222)
                        .name("Test NRQL Condition 2")
                        .nrql(new Nrql("SELECT count(*) from `test2:test-event`"))
                        .enabled(true)
                        .expiration(Expiration.builder()
                                .closeViolationsOnExpiration(false)
                                .expirationDuration(null)
                                .openViolationOnExpiration(false)
                                .build())
                        .signal(Signal.builder()
                                .aggregationDelay("120")
                                .aggregationMethod("EVENT_FLOW")
                                .aggregationTimer("240")
                                .aggregationWindow("60")
                                .fillOption("none")
                                .fillValue(null)
                                .build())
                        .terms(ImmutableList.of(
                                Terms.builder()
                                        .operator("above_or_equals")
                                        .priority("critical")
                                        .threshold("1")
                                        .duration("1")
                                        .timeFunction("any")
                                        .build()
                        ))
                        .valueFunction("sum")
                        .build()
        );
    }

    @Test
    public void create() {
        // given
        executor.addScenario(
                Query.of(fromJson("/AlertsNrqlConditions_create_request.txt")),
                result("/AlertsNrqlConditions_create_response.json"));

        AlertsNrqlCondition requestedCondition = AlertsNrqlCondition.builder()
                .id(11111111)
                .name("Test NRQL Condition")
                .nrql(new Nrql("SELECT count(*) from `test:test-event`"))
                .enabled(true)
                .expiration(Expiration.builder()
                        .closeViolationsOnExpiration(false)
                        .expirationDuration("60")
                        .openViolationOnExpiration(false)
                        .build())
                .signal(Signal.builder()
                        .aggregationDelay("120")
                        .aggregationMethod("CADENCE")
                        .aggregationTimer(null)
                        .aggregationWindow("60")
                        .fillOption("none")
                        .fillValue(null)
                        .build())
                .terms(ImmutableList.of(
                        Terms.builder()
                                .operator("above_or_equals")
                                .priority("critical")
                                .threshold("1")
                                .duration("1")
                                .timeFunction("any")
                                .build()
                ))
                .valueFunction("single_value")
                .build();
        // when
        AlertsNrqlCondition condition = testee.create(POLICY_ID, requestedCondition);

        // then
        assertThat(condition).isEqualTo(requestedCondition);
    }

    @Test
    public void create_shouldThrow_onError() {
        // given
        executor.addScenario(
                Query.of(fromJson("/AlertsNrqlConditions_create_request.txt")),
                result("/AlertsNrqlConditions_create_error.json"));

        AlertsNrqlCondition requestedCondition = AlertsNrqlCondition.builder()
                .id(11111111)
                .name("Test NRQL Condition")
                .nrql(new Nrql("SELECT count(*) from `test:test-event`"))
                .enabled(true)
                .expiration(Expiration.builder()
                        .closeViolationsOnExpiration(false)
                        .expirationDuration("60")
                        .openViolationOnExpiration(false)
                        .build())
                .signal(Signal.builder()
                        .aggregationDelay("120")
                        .aggregationMethod("cadence")
                        .aggregationTimer(null)
                        .aggregationWindow("60")
                        .fillOption("none")
                        .fillValue(null)
                        .build())
                .terms(ImmutableList.of(
                        Terms.builder()
                                .operator("above_or_equals")
                                .priority("critical")
                                .threshold("1")
                                .duration("1")
                                .timeFunction("any")
                                .build()
                ))
                .valueFunction("single_value")
                .build();

        // then
        assertThatThrownBy(() ->
                testee.create(POLICY_ID, requestedCondition)
        ).isInstanceOf(GraphqlApiError.class);
    }

    @Test
    public void delete() {
        // given
        executor
                .addScenario(
                        Query.wrapped(fromJson("/AlertsNrqlConditions_list_request_part1.txt")),
                        result("/AlertsNrqlConditions_list_response_part1.json"))
                .addScenario(
                        Query.wrapped(fromJson("/AlertsNrqlConditions_list_request_part2.txt")),
                        result("/AlertsNrqlConditions_list_response_part2.json"))
                .addScenario(
                        Query.of(fromJson("/AlertsNrqlConditions_delete_request.txt")),
                        result("/AlertsNrqlConditions_delete_response.json"));

        AlertsNrqlCondition expectedCondition = AlertsNrqlCondition.builder()
                .id(11111111)
                .name("Test NRQL Condition 1")
                .nrql(new Nrql("SELECT count(*) from `test1:test-event`"))
                .enabled(true)
                .expiration(Expiration.builder()
                        .closeViolationsOnExpiration(true)
                        .expirationDuration("900")
                        .openViolationOnExpiration(false)
                        .build())
                .signal(Signal.builder()
                        .aggregationDelay("120")
                        .aggregationMethod("EVENT_FLOW")
                        .aggregationTimer(null)
                        .aggregationWindow("60")
                        .fillOption("static")
                        .fillValue("0")
                        .build())
                .terms(ImmutableList.of(
                        Terms.builder()
                                .operator("below")
                                .priority("critical")
                                .threshold("1")
                                .duration("5")
                                .timeFunction("all")
                                .build()
                ))
                .valueFunction("single_value")
                .build();

        // when
        AlertsNrqlCondition deleted = testee.delete(POLICY_ID, 11111111);

        // then
        assertThat(deleted).isEqualTo(expectedCondition);
    }

    @Test
    public void delete_shouldThrow_onError() {
        // given
        executor
                .addScenario(
                        Query.wrapped(fromJson("/AlertsNrqlConditions_list_request_part1.txt")),
                        result("/AlertsNrqlConditions_list_response_part1.json"))
                .addScenario(
                        Query.wrapped(fromJson("/AlertsNrqlConditions_list_request_part2.txt")),
                        result("/AlertsNrqlConditions_list_response_part2.json"))
                .addScenario(
                        Query.of(fromJson("/AlertsNrqlConditions_delete_request.txt")),
                        result("/AlertsNrqlConditions_delete_error.json"));

        // then
        assertThatThrownBy(() ->
                testee.delete(POLICY_ID, 11111111)
        ).isInstanceOf(GraphqlApiError.class);
    }

    @Test
    public void delete_shouldThrow_onNotFound() {
        // given
        executor
                .addScenario(
                        Query.wrapped(fromJson("/AlertsNrqlConditions_list_request_part1.txt")),
                        result("/AlertsNrqlConditions_list_response_part1.json"))
                .addScenario(
                        Query.wrapped(fromJson("/AlertsNrqlConditions_list_request_part2.txt")),
                        result("/AlertsNrqlConditions_list_response_part2.json"));

        // then
        assertThatThrownBy(() ->
                testee.delete(POLICY_ID, 99999999)
        ).isInstanceOf(GraphqlApiError.class);
    }

    @Test
    public void update() {
        // given
        executor.addScenario(
                Query.of(fromJson("/AlertsNrqlConditions_update_request.txt")),
                result("/AlertsNrqlConditions_update_response.json"));

        AlertsNrqlCondition requestedCondition = AlertsNrqlCondition.builder()
                .id(11111111)
                .name("Test NRQL Condition UPDATED")
                .nrql(new Nrql("SELECT count(*) from `test:test-event`"))
                .enabled(true)
                .expiration(Expiration.builder()
                        .closeViolationsOnExpiration(false)
                        .expirationDuration("60")
                        .openViolationOnExpiration(false)
                        .build())
                .signal(Signal.builder()
                        .aggregationDelay("120")
                        .aggregationMethod("CADENCE")
                        .aggregationTimer(null)
                        .aggregationWindow("60")
                        .fillOption("none")
                        .fillValue(null)
                        .build())
                .terms(ImmutableList.of(
                        Terms.builder()
                                .operator("above_or_equals")
                                .priority("critical")
                                .threshold("1")
                                .duration("1")
                                .timeFunction("any")
                                .build()
                ))
                .valueFunction("single_value")
                .build();
        // when
        AlertsNrqlCondition condition = testee.update(11111111, requestedCondition);

        // then
        assertThat(condition).isEqualTo(requestedCondition);
    }

    @Test
    public void update_shouldThrow_onError() {
        // given
        executor.addScenario(
                Query.of(fromJson("/AlertsNrqlConditions_update_request.txt")),
                result("/AlertsNrqlConditions_update_error.json"));

        AlertsNrqlCondition requestedCondition = AlertsNrqlCondition.builder()
                .id(11111111)
                .name("Test NRQL Condition UPDATED")
                .nrql(new Nrql("SELECT count(*) from `test:test-event`"))
                .enabled(true)
                .expiration(Expiration.builder()
                        .closeViolationsOnExpiration(false)
                        .expirationDuration("60")
                        .openViolationOnExpiration(false)
                        .build())
                .signal(Signal.builder()
                        .aggregationDelay("120")
                        .aggregationMethod("CADENCE")
                        .aggregationTimer(null)
                        .aggregationWindow("60")
                        .fillOption("none")
                        .fillValue(null)
                        .build())
                .terms(ImmutableList.of(
                        Terms.builder()
                                .operator("above_or_equals")
                                .priority("critical")
                                .threshold("1")
                                .duration("1")
                                .timeFunction("any")
                                .build()
                ))
                .valueFunction("single_value")
                .build();

        // then
        assertThatThrownBy(() ->
                testee.update(11111111, requestedCondition)
        ).isInstanceOf(GraphqlApiError.class);
        ;
    }

    private static Result result(String fileName) {
        return fromJson(fileName, Result.class);
    }
}