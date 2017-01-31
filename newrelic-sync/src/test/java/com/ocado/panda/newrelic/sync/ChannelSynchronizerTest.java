package com.ocado.panda.newrelic.sync;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.ocado.panda.newrelic.api.model.channels.AlertsChannel;
import com.ocado.panda.newrelic.api.model.channels.AlertsChannelLinks;
import com.ocado.panda.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.panda.newrelic.api.model.policies.AlertsPolicyChannels;
import com.ocado.panda.newrelic.sync.configuration.PolicyConfiguration;
import com.ocado.panda.newrelic.sync.configuration.channel.Channel;
import com.ocado.panda.newrelic.sync.configuration.channel.EmailChannel;
import com.ocado.panda.newrelic.sync.configuration.channel.SlackChannel;
import com.ocado.panda.newrelic.sync.exception.NewRelicSyncException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

import java.util.Optional;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

public class ChannelSynchronizerTest extends AbstractSynchronizerTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private static final int POLICY_ID = 42;
    private static final String POLICY_NAME = "policyName";
    private static final AlertsPolicy POLICY = AlertsPolicy.builder()
            .id(POLICY_ID)
            .name(POLICY_NAME)
            .build();

    private static final String EMAIL_CHANNEL_NAME = "emailChannel";
    private static final String SLACK_CHANNEL_NAME = "slackChannel";
    private static final Channel EMAIL_CHANNEL = EmailChannel.builder()
            .channelName(EMAIL_CHANNEL_NAME)
            .emailAddress("recipients")
            .build();
    private static final Channel SLACK_CHANNEL = SlackChannel.builder()
            .channelName(SLACK_CHANNEL_NAME)
            .slackUrl("url")
            .build();

    private static final AlertsChannel CONFIGURED_EMAIL_CHANNEL = createAlertChannel(EMAIL_CHANNEL);
    private static final AlertsChannel CONFIGURED_SLACK_CHANNEL = createAlertChannel(SLACK_CHANNEL);
    private static final AlertsChannel SAVED_EMAIL_CHANNEL = createAlertChannel(1, EMAIL_CHANNEL);
    private static final AlertsChannel SAVED_SLACK_CHANNEL = createAlertChannel(2, SLACK_CHANNEL);

    private ChannelSynchronizer testee;

    @Before
    public void setUp() {
        testee = new ChannelSynchronizer(apiMock);

        when(alertsChannelsApiMock.create(CONFIGURED_EMAIL_CHANNEL)).thenReturn(SAVED_EMAIL_CHANNEL);
        when(alertsChannelsApiMock.create(CONFIGURED_SLACK_CHANNEL)).thenReturn(SAVED_SLACK_CHANNEL);
        when(alertsPoliciesApiMock.getByName(POLICY_NAME)).thenReturn(Optional.of(POLICY));
    }

    @Test
    public void shouldThrowException_whenPolicyDoesNotExist() {
        // given
        when(alertsPoliciesApiMock.getByName(POLICY_NAME)).thenReturn(Optional.empty());
        PolicyConfiguration policyConfiguration = PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .build();

        // then - exception
        expectedException.expect(NewRelicSyncException.class);
        expectedException.expectMessage(format("Policy %s does not exist", POLICY_NAME));

        // when
        testee.sync(policyConfiguration);
    }

    @Test
    public void shouldDoNothing_whenNoChannelsInConfiguration() {
        // given
        PolicyConfiguration policyConfiguration = PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .build();

        // when
        testee.sync(policyConfiguration);

        // then
        InOrder order = inOrder(alertsChannelsApiMock);
        order.verify(alertsChannelsApiMock).list();
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldCreateRequiredChannels() {
        // given
        PolicyConfiguration policyConfiguration = PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .channel(EMAIL_CHANNEL)
                .channel(SLACK_CHANNEL)
                .build();

        // when
        testee.sync(policyConfiguration);

        // then
        InOrder order = inOrder(alertsChannelsApiMock);
        order.verify(alertsChannelsApiMock).list();
        order.verify(alertsChannelsApiMock).create(CONFIGURED_EMAIL_CHANNEL);
        order.verify(alertsChannelsApiMock).create(CONFIGURED_SLACK_CHANNEL);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldRemoveOldChannelsAndCreateNewOne_whenChannelUpdated() {
        // given
        int updatedEmailChannelId = 10;
        EmailChannel updatedEmailChannel = EmailChannel.builder()
                .channelName(EMAIL_CHANNEL_NAME)
                .emailAddress("different recipients")
                .build();
        AlertsChannel updatedEmailAlertChannel = createAlertChannel(updatedEmailChannel);
        when(alertsChannelsApiMock.create(updatedEmailAlertChannel))
                .thenReturn(createAlertChannel(updatedEmailChannelId, updatedEmailChannel));

        AlertsChannel emailChannelInPolicy = channelInPolicy(SAVED_EMAIL_CHANNEL, POLICY_ID);
        when(alertsChannelsApiMock.list())
                .thenReturn(ImmutableList.of(emailChannelInPolicy, channelInPolicy(SAVED_SLACK_CHANNEL, POLICY_ID)));
        when(alertsChannelsApiMock.deleteFromPolicy(POLICY_ID, emailChannelInPolicy.getId()))
                .thenReturn(emailChannelInPolicy);

        PolicyConfiguration policyConfiguration = PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .channel(updatedEmailChannel)
                .channel(SLACK_CHANNEL)
                .build();

        // when
        testee.sync(policyConfiguration);

        // then
        AlertsPolicyChannels expected = AlertsPolicyChannels.builder()
                .policyId(POLICY_ID)
                .channelIds(ImmutableSet.of(updatedEmailChannelId, SAVED_SLACK_CHANNEL.getId()))
                .build();

        InOrder order = inOrder(alertsChannelsApiMock, alertsPoliciesApiMock);
        order.verify(alertsChannelsApiMock).list();
        order.verify(alertsChannelsApiMock).create(updatedEmailAlertChannel);
        order.verify(alertsPoliciesApiMock).updateChannels(expected);
        order.verify(alertsChannelsApiMock).deleteFromPolicy(POLICY_ID, SAVED_EMAIL_CHANNEL.getId());
        order.verify(alertsChannelsApiMock).delete(SAVED_EMAIL_CHANNEL.getId());
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldNotRemoveUnsuedChannel_whenChannelBelongsToDifferentPolicy() {
        // given
        AlertsChannel emailChannelInPolicy = channelInPolicy(SAVED_EMAIL_CHANNEL, POLICY_ID, POLICY_ID + 1);
        when(alertsChannelsApiMock.list()).thenReturn(ImmutableList.of(
                emailChannelInPolicy,
                channelInPolicy(SAVED_SLACK_CHANNEL, POLICY_ID)
        ));
        when(alertsChannelsApiMock.deleteFromPolicy(POLICY_ID, emailChannelInPolicy.getId()))
                .thenReturn(emailChannelInPolicy);

        // when
        PolicyConfiguration policyConfiguration = PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .channel(SLACK_CHANNEL)
                .build();
        testee.sync(policyConfiguration);

        // then
        AlertsPolicyChannels expected = AlertsPolicyChannels.builder()
                .policyId(POLICY_ID)
                .channelIds(ImmutableSet.of(SAVED_SLACK_CHANNEL.getId()))
                .build();

        InOrder order = inOrder(alertsChannelsApiMock, alertsPoliciesApiMock);
        order.verify(alertsChannelsApiMock).list();
        order.verify(alertsPoliciesApiMock).updateChannels(expected);
        order.verify(alertsChannelsApiMock).deleteFromPolicy(POLICY_ID, SAVED_EMAIL_CHANNEL.getId());
        order.verify(alertsChannelsApiMock, never()).delete(SAVED_EMAIL_CHANNEL.getId());
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldRemoveUnusedPolicyChannel() {
        // given
        AlertsChannel emailChannelInPolicy = channelInPolicy(SAVED_EMAIL_CHANNEL, POLICY_ID);
        when(alertsChannelsApiMock.list())
                .thenReturn(ImmutableList.of(emailChannelInPolicy, channelInPolicy(SAVED_SLACK_CHANNEL, POLICY_ID)));
        when(alertsChannelsApiMock.deleteFromPolicy(POLICY_ID, emailChannelInPolicy.getId()))
                .thenReturn(emailChannelInPolicy);

        // when
        PolicyConfiguration policyConfiguration = PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .channel(SLACK_CHANNEL)
                .build();
        testee.sync(policyConfiguration);

        // then
        AlertsPolicyChannels expected = AlertsPolicyChannels.builder()
                .policyId(POLICY_ID)
                .channelIds(ImmutableSet.of(SAVED_SLACK_CHANNEL.getId()))
                .build();

        InOrder order = inOrder(alertsChannelsApiMock, alertsPoliciesApiMock);
        order.verify(alertsChannelsApiMock).list();
        order.verify(alertsPoliciesApiMock).updateChannels(expected);
        order.verify(alertsChannelsApiMock).deleteFromPolicy(POLICY_ID, SAVED_EMAIL_CHANNEL.getId());
        order.verify(alertsChannelsApiMock).delete(SAVED_EMAIL_CHANNEL.getId());
        order.verifyNoMoreInteractions();
    }

    private static AlertsChannel createAlertChannel(Channel channel) {
        return createAlertChannel(null, channel);
    }

    private static AlertsChannel createAlertChannel(Integer id, Channel channel) {
        return AlertsChannel.builder()
                .id(id)
                .name(channel.getChannelName())
                .type(channel.getTypeString())
                .configuration(channel.getChannelTypeSupport().generateAlertsChannelConfiguration())
                .links(new AlertsChannelLinks(emptyList()))
                .build();
    }

    private static AlertsChannel channelInPolicy(AlertsChannel channel, Integer... policyIds) {
        return AlertsChannel.builder()
                .id(channel.getId())
                .name(channel.getName())
                .type(channel.getType())
                .configuration(channel.getConfiguration())
                .links(new AlertsChannelLinks(ImmutableList.copyOf(policyIds)))
                .build();
    }
}
