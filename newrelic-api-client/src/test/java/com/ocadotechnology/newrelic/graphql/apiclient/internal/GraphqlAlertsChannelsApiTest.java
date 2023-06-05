package com.ocadotechnology.newrelic.graphql.apiclient.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.ocadotechnology.newrelic.apiclient.model.channels.AlertsChannel;
import com.ocadotechnology.newrelic.apiclient.model.channels.AlertsChannelConfiguration;
import com.ocadotechnology.newrelic.apiclient.model.channels.AlertsChannelLinks;
import com.ocadotechnology.newrelic.graphql.apiclient.GraphqlApiError;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.testutil.QueryExecutorMock;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Query;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Result;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ocadotechnology.newrelic.graphql.apiclient.internal.testutil.TestResources.fromJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

public class GraphqlAlertsChannelsApiTest {
    private static final int ACCOUNT_ID = 12345678;

    private final QueryExecutorMock executor = new QueryExecutorMock();
    private GraphqlAlertsChannelsApi testee;

    @Before
    public void setUp() {
        testee = new GraphqlAlertsChannelsApi(executor, ACCOUNT_ID);
    }

    @Test
    public void getById_email() {
        // given
        executor.addScenario(
                Query.wrapped(fromJson("/AlertChannels_getById_request.txt")),
                result("/AlertChannels_getById_email_response.json"));

        // when
        Optional<AlertsChannel> channelOptional = testee.getById(1111111);

        // then
        assertThat(channelOptional).isNotEmpty();
        assertThat(channelOptional.get()).usingRecursiveComparison().isEqualTo(AlertsChannel.builder()
                .id(1111111)
                .name("Test Email Channel")
                .type("email")
                .configuration(AlertsChannelConfiguration.builder()
                        .recipients("email@domain.com,other@domain.com")
                        .includeJsonAttachment(true)
                        .build())
                .links(new AlertsChannelLinks(ImmutableList.of(1234567)))
                .build());
    }

    @Test
    public void getById_user() {
        // given
        executor.addScenario(
                Query.wrapped(fromJson("/AlertChannels_getById_request.txt")),
                result("/AlertChannels_getById_user_response.json"));

        // when
        Optional<AlertsChannel> channelOptional = testee.getById(1111111);

        // then
        assertThat(channelOptional).isNotEmpty();
        assertThat(channelOptional.get()).usingRecursiveComparison().isEqualTo(AlertsChannel.builder()
                .id(1111111)
                .name("Test User Channel")
                .type("user")
                .configuration(AlertsChannelConfiguration.builder()
                        .userId(2111111111)
                        .build())
                .links(new AlertsChannelLinks(new ArrayList<>()))
                .build());
    }

    @Test
    public void getById_slack() {
        // given
        executor.addScenario(
                Query.wrapped(fromJson("/AlertChannels_getById_request.txt")),
                result("/AlertChannels_getById_slack_response.json"));

        // when
        Optional<AlertsChannel> channelOptional = testee.getById(1111111);

        // then
        assertThat(channelOptional).isNotEmpty();
        assertThat(channelOptional.get()).usingRecursiveComparison().isEqualTo(AlertsChannel.builder()
                .id(1111111)
                .name("Test Slack Channel")
                .type("slack")
                .configuration(AlertsChannelConfiguration.builder()
                        .channel("test-slack-channel")
                        .build())
                .links(new AlertsChannelLinks(new ArrayList<>()))
                .build());
    }

    @Test
    public void getById_pagerDuty() {
        // given
        executor.addScenario(
                Query.wrapped(fromJson("/AlertChannels_getById_request.txt")),
                result("/AlertChannels_getById_pagerduty_response.json"));

        // when
        Optional<AlertsChannel> channelOptional = testee.getById(1111111);

        // then
        assertThat(channelOptional).isNotEmpty();
        assertThat(channelOptional.get()).usingRecursiveComparison().isEqualTo(AlertsChannel.builder()
                .id(1111111)
                .name("Test Pagerduty Channel")
                .type("pagerduty")
                .links(new AlertsChannelLinks(new ArrayList<>()))
                .build());
    }

    @Test
    public void getById_json_webhook() {
        // given
        executor.addScenario(
                Query.wrapped(fromJson("/AlertChannels_getById_request.txt")),
                result("/AlertChannels_getById_webhook_json_response.json"));

        // when
        Optional<AlertsChannel> channelOptional = testee.getById(1111111);

        // then
        assertThat(channelOptional).isNotEmpty();
        assertThat(channelOptional.get()).usingRecursiveComparison().isEqualTo(AlertsChannel.builder()
                .id(1111111)
                .name("Test Webhook Channel")
                .type("webhook")
                .configuration(AlertsChannelConfiguration.builder()
                        .baseUrl("https://address.domain.com")
                        .headers(ImmutableMap.of(
                                "Custom-Header", "CUSTOM-VALUE",
                                "Custom-Header-2", "CUSTOM-VALUE-2"))
                        .payloadType("application/json")
                        .payload(ImmutableMap.of("field", "value"))
                        .build())
                .links(new AlertsChannelLinks(new ArrayList<>()))
                .build());
    }

    @Test
    public void getById_form_webhook() {
        // given
        executor.addScenario(
                Query.wrapped(fromJson("/AlertChannels_getById_request.txt")),
                result("/AlertChannels_getById_webhook_form_response.json"));

        // when
        Optional<AlertsChannel> channelOptional = testee.getById(1111111);

        // then
        assertThat(channelOptional).isNotEmpty();
        assertThat(channelOptional.get()).usingRecursiveComparison().isEqualTo(AlertsChannel.builder()
                .id(1111111)
                .name("Test Webhook Channel")
                .type("webhook")
                .configuration(AlertsChannelConfiguration.builder()
                        .baseUrl("https://address.domain.com")
                        .authUsername("test")
                        .payloadType("application/x-www-form-urlencoded")
                        .payload(ImmutableMap.of(
                                "current_state", "acknowledged",
                                "account_name", "$ACCOUNT_NAME"))
                        .build())
                .links(new AlertsChannelLinks(new ArrayList<>()))
                .build());
    }

    @Test
    public void getById_shouldReturnEmpty_whenChannelNotFound() {
        // given
        executor.addScenario(
                Query.wrapped(fromJson("/AlertChannels_getById_request.txt")),
                result("/AlertChannels_getById_notFound.json"));

        // when
        Optional<AlertsChannel> channelOptional = testee.getById(1111111);

        // then
        assertThat(channelOptional).isEmpty();
    }

    @Test
    public void list() {
        // given
        executor
                .addScenario(
                        Query.wrapped(fromJson("/AlertChannels_list_request_part1.txt")),
                        result("/AlertChannels_list_response_part1.json"))
                .addScenario(
                        Query.wrapped(fromJson("/AlertChannels_list_request_part2.txt")),
                        result("/AlertChannels_list_response_part2.json"));

        // when
        List<AlertsChannel> channels = testee.list();

        // then
        assertThat(channels).hasSize(3);
        assertThat(channels).extracting(AlertsChannel::getId, AlertsChannel::getType).containsExactly(
                tuple(1111111, "email"),
                tuple(2222222, "webhook"),
                tuple(3333333, "slack"));
    }

    @Test
    public void list_shouldThrow_onError() {
        // given
        executor.addScenario(
                Query.wrapped(fromJson("/AlertChannels_list_request_part1.txt")),
                result("/AlertChannels_list_error.json"));

        // then
        assertThatThrownBy(() -> testee.list()).isInstanceOf(GraphqlApiError.class);
    }

    @Test
    public void deleteFromPolicy() {
        // given
        executor
                .addScenario(
                        Query.wrapped(fromJson("/AlertChannels_getById_request.txt")),
                        result("/AlertChannels_getById_email_response.json"))
                .addScenario(
                        Query.of(fromJson("/AlertChannels_deleteFromPolicy_request.txt")),
                        result("/AlertChannels_deleteFromPolicy_response.json"));

        // when
        AlertsChannel channel = testee.deleteFromPolicy(7654321, 1111111);

        // then
        assertThat(channel).usingRecursiveComparison().isEqualTo(AlertsChannel.builder()
                .id(1111111)
                .name("Test Email Channel")
                .type("email")
                .configuration(AlertsChannelConfiguration.builder()
                        .recipients("email@domain.com,other@domain.com")
                        .includeJsonAttachment(true)
                        .build())
                .links(new AlertsChannelLinks(ImmutableList.of(1234567)))
                .build());
    }

    @Test
    public void deleteFromPolicy_shouldThrow_onError() {
        // given
        executor
                .addScenario(
                        Query.wrapped(fromJson("/AlertChannels_getById_request.txt")),
                        result("/AlertChannels_getById_email_response.json"))
                .addScenario(
                        Query.of(fromJson("/AlertChannels_deleteFromPolicy_request.txt")),
                        result("/AlertChannels_deleteFromPolicy_error.json"));

        // then
        assertThatThrownBy(() -> testee.deleteFromPolicy(7654321, 1111111))
                .isInstanceOf(GraphqlApiError.class);
    }

    @Test
    public void create_email() {
        // given
        executor
                .addScenario(
                        Query.of(fromJson("/AlertChannels_create_email_request.txt")),
                        result("/AlertChannels_create_email_response.json"));

        // when
        AlertsChannel channel = testee.create(AlertsChannel.builder()
                .name("Test Email Channel")
                .type("email")
                .configuration(AlertsChannelConfiguration.builder()
                        .recipients("email@domain.com,other@domain.com")
                        .build())
                .build());

        // then
        assertThat(channel).usingRecursiveComparison().isEqualTo(AlertsChannel.builder()
                .id(1111111)
                .name("Test Email Channel")
                .type("email")
                .configuration(AlertsChannelConfiguration.builder()
                        .recipients("email@domain.com,other@domain.com")
                        .includeJsonAttachment(false)
                        .build())
                .links(new AlertsChannelLinks(new ArrayList<>()))
                .build());
    }

    @Test
    public void create_pagerduty() {
        // given
        executor
                .addScenario(
                        Query.of(fromJson("/AlertChannels_create_pagerduty_request.txt")),
                        result("/AlertChannels_create_pagerduty_response.json"));

        // when
        AlertsChannel channel = testee.create(AlertsChannel.builder()
                .name("Test PagerDuty Channel")
                .type("pagerduty")
                .configuration(AlertsChannelConfiguration.builder()
                        .serviceKey("abcdefghijklmnopqRSTUVWXYZ")
                        .build())
                .build());

        // then
        assertThat(channel).usingRecursiveComparison().isEqualTo(AlertsChannel.builder()
                .id(1111111)
                .name("Test PagerDuty Channel")
                .type("pagerduty")
                .links(new AlertsChannelLinks(new ArrayList<>()))
                .build());
    }

    @Test
    public void create_slack() {
        // given
        executor
                .addScenario(
                        Query.of(fromJson("/AlertChannels_create_slack_request.txt")),
                        result("/AlertChannels_create_slack_response.json"));

        // when
        AlertsChannel channel = testee.create(AlertsChannel.builder()
                .name("Test Slack Channel")
                .type("slack")
                .configuration(AlertsChannelConfiguration.builder()
                        .channel("the channel")
                        .url("https://domain.com/slack")
                        .build())
                .build());

        // then
        assertThat(channel).usingRecursiveComparison().isEqualTo(AlertsChannel.builder()
                .id(1111111)
                .name("Test Slack Channel")
                .type("slack")
                .configuration(AlertsChannelConfiguration.builder()
                        .channel("the channel")
                        .url("***************om/slack")
                        .build())
                .links(new AlertsChannelLinks(new ArrayList<>()))
                .build());
    }

    @Test
    public void create_json_webhook() {
        // given
        executor
                .addScenario(
                        Query.of(fromJson("/AlertChannels_create_webhook_json_request.txt")),
                        result("/AlertChannels_create_webhook_json_response.json"));

        // when
        AlertsChannel channel = testee.create(AlertsChannel.builder()
                .name("Test Webhook Channel")
                .type("webhook")
                .configuration(AlertsChannelConfiguration.builder()
                        .baseUrl("https://address.domain.com")
                        .headers(ImmutableMap.of(
                                "Custom-Header", "CUSTOM-VALUE",
                                "Custom-Header-2", "CUSTOM-VALUE-2"))
                        .payloadType("application/json")
                        .payload(ImmutableMap.of("field", "value"))
                        .build())
                .links(new AlertsChannelLinks(new ArrayList<>()))
                .build());

        // then
        assertThat(channel).usingRecursiveComparison().isEqualTo(AlertsChannel.builder()
                .id(1111111)
                .name("Test Webhook Channel")
                .type("webhook")
                .configuration(AlertsChannelConfiguration.builder()
                        .baseUrl("https://address.domain.com")
                        .payloadType("application/json")
                        .payload(ImmutableMap.of("field", "value"))
                        .headers(ImmutableMap.of(
                                "Custom-Header", "CUSTOM-VALUE",
                                "Custom-Header-2", "CUSTOM-VALUE-2"))
                        .build())
                .links(new AlertsChannelLinks(new ArrayList<>()))
                .build());
    }

    @Test
    public void create_form_webhook() {
        // given
        executor
                .addScenario(
                        Query.of(fromJson("/AlertChannels_create_webhook_form_request.txt")),
                        result("/AlertChannels_create_webhook_form_response.json"));

        // when
        AlertsChannel channel = testee.create(AlertsChannel.builder()
                .name("Test Webhook Channel")
                .type("webhook")
                .configuration(AlertsChannelConfiguration.builder()
                        .baseUrl("https://address.domain.com")
                        .authUsername("test")
                        .authPassword("pass")
                        .payloadType("application/x-www-form-urlencoded")
                        .payload(ImmutableMap.of(
                                "current_state", "acknowledged",
                                "account_name", "$ACCOUNT_NAME"))
                        .build())
                .links(new AlertsChannelLinks(new ArrayList<>()))
                .build());

        // then
        assertThat(channel).usingRecursiveComparison().isEqualTo(AlertsChannel.builder()
                .id(1111111)
                .name("Test Webhook Channel")
                .type("webhook")
                .configuration(AlertsChannelConfiguration.builder()
                        .baseUrl("https://address.domain.com")
                        .authUsername("test")
                        .payloadType("application/x-www-form-urlencoded")
                        .payload(ImmutableMap.of(
                                "current_state", "acknowledged",
                                "account_name", "$ACCOUNT_NAME"))
                        .build())
                .links(new AlertsChannelLinks(new ArrayList<>()))
                .build());
    }

    @Test
    public void create_shouldThrow_onError() {
        // given
        executor
                .addScenario(
                        Query.of(fromJson("/AlertChannels_create_email_request.txt")),
                        result("/AlertChannels_create_error.json"));

        // then
        assertThatThrownBy(() -> testee.create(AlertsChannel.builder()
                .name("Test Email Channel")
                .type("email")
                .configuration(AlertsChannelConfiguration.builder()
                        .recipients("email@domain.com,other@domain.com")
                        .build())
                .build())).isInstanceOf(GraphqlApiError.class);
    }

    @Test
    public void delete() {
        // given
        executor
                .addScenario(
                        Query.wrapped(fromJson("/AlertChannels_getById_request.txt")),
                        result("/AlertChannels_getById_email_response.json"))
                .addScenario(
                        Query.of(fromJson("/AlertChannels_delete_request.txt")),
                        result("/AlertChannels_delete_response.json"));

        // when
        AlertsChannel channel = testee.delete(1111111);

        // then
        assertThat(channel).usingRecursiveComparison().isEqualTo(AlertsChannel.builder()
                .id(1111111)
                .name("Test Email Channel")
                .type("email")
                .configuration(AlertsChannelConfiguration.builder()
                        .recipients("email@domain.com,other@domain.com")
                        .includeJsonAttachment(true)
                        .build())
                .links(new AlertsChannelLinks(ImmutableList.of(1234567)))
                .build());
    }

    @Test
    public void delete_shouldThrow_onError() {
        // given
        executor
                .addScenario(
                        Query.wrapped(fromJson("/AlertChannels_getById_request.txt")),
                        result("/AlertChannels_getById_email_response.json"))
                .addScenario(
                        Query.of(fromJson("/AlertChannels_delete_request.txt")),
                        result("/AlertChannels_delete_error.json"));

        // then
        assertThatThrownBy(() -> testee.delete(1111111))
                .isInstanceOf(GraphqlApiError.class);
    }

    private static Result result(String fileName) {
        return fromJson(fileName, Result.class);
    }
}