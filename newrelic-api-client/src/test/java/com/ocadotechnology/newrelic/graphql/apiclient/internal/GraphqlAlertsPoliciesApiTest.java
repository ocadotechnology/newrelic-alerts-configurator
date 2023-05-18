package com.ocadotechnology.newrelic.graphql.apiclient.internal;

import com.google.common.collect.ImmutableList;
import com.ocadotechnology.newrelic.apiclient.model.policies.AlertsPolicy;
import com.ocadotechnology.newrelic.apiclient.model.policies.AlertsPolicyChannels;
import com.ocadotechnology.newrelic.graphql.apiclient.GraphqlApiError;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.testutil.QueryExecutorMock;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Query;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Result;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static com.ocadotechnology.newrelic.graphql.apiclient.internal.testutil.TestResources.fromJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GraphqlAlertsPoliciesApiTest {
    private static final int ACCOUNT_ID = 12345678;

    private final QueryExecutorMock executor = new QueryExecutorMock();
    private GraphqlAlertsPoliciesApi testee;

    @Before
    public void setUp() {
        testee = new GraphqlAlertsPoliciesApi(executor, ACCOUNT_ID);
    }

    @Test
    public void getByName_shouldReturnPolicy() {
        // given
        executor.addScenario(
                Query.wrapped(fromJson("/AlertPolicies_getByName_request.txt")),
                result("/AlertPolicies_getByName_singleResponse.json"));

        // when
        Optional<AlertsPolicy> policyOptional = testee.getByName("Test Policy");

        // then
        assertThat(policyOptional).isNotEmpty();
        assertThat(policyOptional.get()).usingRecursiveComparison().isEqualTo(AlertsPolicy.builder()
                .id(7654321)
                .name("Test Policy")
                .incidentPreference("PER_CONDITION")
                .build());
    }

    @Test
    public void getByName_shouldReturnPolicy_whenClientReturnsNotUniqueResult() {
        // given
        executor.addScenario(
                Query.wrapped(fromJson("/AlertPolicies_getByName_request.txt")),
                result("/AlertPolicies_getByName_multiResponse.json"));

        // when
        Optional<AlertsPolicy> policyOptional = testee.getByName("Test Policy");

        // then
        assertThat(policyOptional).isNotEmpty();
        assertThat(policyOptional.get()).usingRecursiveComparison().isEqualTo(AlertsPolicy.builder()
                .id(7654321)
                .name("Test Policy")
                .incidentPreference("PER_CONDITION")
                .build());
    }

    @Test
    public void getById_shouldReturnPolicy() {
        // given
        executor.addScenario(
                Query.wrapped(fromJson("/AlertPolicies_getById_request.txt")),
                result("/AlertPolicies_getById_response.json"));

        // when
        Optional<AlertsPolicy> policyOptional = testee.getById(7654321);

        // then
        assertThat(policyOptional).isNotEmpty();
        assertThat(policyOptional.get()).usingRecursiveComparison().isEqualTo(AlertsPolicy.builder()
                .id(7654321)
                .name("Test Policy")
                .incidentPreference("PER_CONDITION")
                .build());
    }

    @Test
    public void getById_shouldReturnEmpty_whenPolicyNotFound() {
        // given
        executor.addScenario(
                Query.wrapped(fromJson("/AlertPolicies_getById_request.txt")),
                result("/AlertPolicies_getById_notFound.json"));

        // when
        Optional<AlertsPolicy> policyOptional = testee.getById(7654321);

        // then
        assertThat(policyOptional).isEmpty();
    }

    @Test
    public void getById_shouldThrow_onError() {
        // given
        executor.addScenario(
                Query.wrapped(fromJson("/AlertPolicies_getById_request.txt")),
                result("/AlertPolicies_getById_accessDenied.json"));

        // then
        assertThatThrownBy(() -> testee.getById(7654321)).isInstanceOf(GraphqlApiError.class);
    }

    @Test
    public void create() {
        // given
        executor.addScenario(
                Query.of(fromJson("/AlertPolicies_create_request.txt")),
                result("/AlertPolicies_create_response.json"));

        // when
        AlertsPolicy policy = testee.create(AlertsPolicy.builder()
                .name("Test Policy")
                .incidentPreference("PER_CONDITION")
                .build());

        // then
        assertThat(policy).usingRecursiveComparison().isEqualTo(AlertsPolicy.builder()
                .id(7654321)
                .name("Test Policy")
                .incidentPreference("PER_CONDITION")
                .build());
    }

    @Test
    public void delete() {
        // given
        executor.addScenario(
                        Query.wrapped(fromJson("/AlertPolicies_getById_request.txt")),
                        result("/AlertPolicies_getById_response.json"))
                .addScenario(
                        Query.of(fromJson("/AlertPolicies_delete_request.txt")),
                        result("/AlertPolicies_delete_response.json"));

        // when
        AlertsPolicy policy = testee.delete(7654321);

        // then
        assertThat(policy).usingRecursiveComparison().isEqualTo(AlertsPolicy.builder()
                .id(7654321)
                .name("Test Policy")
                .incidentPreference("PER_CONDITION")
                .build());
    }

    @Test
    public void delete_shouldThrow_whenNotFound() {
        // given
        executor.addScenario(
                        Query.wrapped(fromJson("/AlertPolicies_getById_request.txt")),
                        result("/AlertPolicies_getById_notFound.json"))
                .addScenario(
                        Query.of(fromJson("/AlertPolicies_delete_request.txt")),
                        result("/AlertPolicies_delete_response.json"));

        // then
        assertThatThrownBy(() -> testee.delete(7654321)).isInstanceOf(GraphqlApiError.class);
    }

    @Test
    public void updatePolicyChannels() {
        // given
        executor.addScenario(
                Query.of(fromJson("/AlertPolicies_updateChannels_request.txt")),
                result("/AlertPolicies_updateChannels_response.json"));

        // when
        AlertsPolicyChannels channels = testee.updateChannels(AlertsPolicyChannels.builder()
                .policyId(7654321)
                .channelIds(ImmutableList.of(1111111, 2222222))
                .build());

        // then
        assertThat(channels).usingRecursiveComparison().isEqualTo(AlertsPolicyChannels.builder()
                .policyId(7654321)
                .channelIds(ImmutableList.of(1111111, 2222222))
                .build());
    }

    @Test
    public void updatePolicyChannels_shouldThrow_onError() {
        // given
        executor.addScenario(
                Query.of(fromJson("/AlertPolicies_updateChannels_request.txt")),
                result("/AlertPolicies_updateChannels_error.json"));

        // then
        assertThatThrownBy(() -> testee.updateChannels(AlertsPolicyChannels.builder()
                .policyId(7654321)
                .channelIds(ImmutableList.of(1111111, 2222222))
                .build())
        ).isInstanceOf(GraphqlApiError.class);
    }

    private static Result result(String fileName) {
        return fromJson(fileName, Result.class);
    }
}
