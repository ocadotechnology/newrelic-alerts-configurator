package com.ocadotechnology.newrelic.alertsconfigurator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.Channel;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.EmailChannel;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.SlackChannel;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.UserChannel;
import com.ocadotechnology.newrelic.alertsconfigurator.exception.NewRelicSyncException;
import com.ocadotechnology.newrelic.api.model.channels.AlertsChannel;
import com.ocadotechnology.newrelic.api.model.channels.AlertsChannelLinks;
import com.ocadotechnology.newrelic.api.model.policies.AlertsPolicy;
import com.ocadotechnology.newrelic.api.model.policies.AlertsPolicyChannels;
import com.ocadotechnology.newrelic.api.model.users.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

import java.util.Optional;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.mockito.Mockito.*;

public class ChannelConfiguratorTest extends AbstractConfiguratorTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private static final int POLICY_ID = 42;
    private static final String POLICY_NAME = "policyName";
    private static final AlertsPolicy POLICY = AlertsPolicy.builder()
            .id(POLICY_ID)
            .name(POLICY_NAME)
            .build();

    private static final String USER_EMAIL = "test@test";
    private static final int USER_ID = 123;

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
    private static final Channel USER_CHANNEL = UserChannel.builder()
            .userEmail(USER_EMAIL)
            .build();

    private final AlertsChannel configuredEmailChannel = createAlertChannel(EMAIL_CHANNEL);
    private final AlertsChannel configuredSlackChannel = createAlertChannel(SLACK_CHANNEL);
    private final AlertsChannel savedEmailChannel = createAlertChannel(1, EMAIL_CHANNEL);
    private final AlertsChannel savedSlackChannel = createAlertChannel(2, SLACK_CHANNEL);
    private AlertsChannel savedUserChannel;

    private ChannelConfigurator testee;

    @Before
    public void setUp() {
        testee = new ChannelConfigurator(apiMock);

        when(alertsChannelsApiMock.create(configuredEmailChannel)).thenReturn(savedEmailChannel);
        when(alertsChannelsApiMock.create(configuredSlackChannel)).thenReturn(savedSlackChannel);
        when(alertsPoliciesApiMock.getByName(POLICY_NAME)).thenReturn(Optional.of(POLICY));

        User user = User.builder()
                .id(USER_ID)
                .email(USER_EMAIL)
                .build();
        when(usersApiMock.getByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        savedUserChannel = createAlertChannel(3, USER_CHANNEL);
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
        when(alertsChannelsApiMock.list()).thenReturn(ImmutableList.of(savedUserChannel));
        PolicyConfiguration policyConfiguration = PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .channel(EMAIL_CHANNEL)
                .channel(SLACK_CHANNEL)
                .build();

        // when
        testee.sync(policyConfiguration);

        // then
        AlertsPolicyChannels expected = AlertsPolicyChannels.builder()
                .policyId(POLICY_ID)
                .channelIds(ImmutableSet.of(savedEmailChannel.getId(), savedSlackChannel.getId()))
                .build();

        InOrder order = inOrder(alertsChannelsApiMock, alertsPoliciesApiMock);
        order.verify(alertsChannelsApiMock).list();
        order.verify(alertsChannelsApiMock).create(configuredEmailChannel);
        order.verify(alertsChannelsApiMock).create(configuredSlackChannel);
        order.verify(alertsPoliciesApiMock).updateChannels(expected);
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

        AlertsChannel emailChannelInPolicy = channelInPolicy(savedEmailChannel, POLICY_ID);
        when(alertsChannelsApiMock.list()).thenReturn(ImmutableList.of(
                savedUserChannel,
                emailChannelInPolicy,
                channelInPolicy(savedSlackChannel, POLICY_ID)
        ));
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
                .channelIds(ImmutableSet.of(updatedEmailChannelId, savedSlackChannel.getId()))
                .build();

        InOrder order = inOrder(alertsChannelsApiMock, alertsPoliciesApiMock);
        order.verify(alertsChannelsApiMock).list();
        order.verify(alertsChannelsApiMock).create(updatedEmailAlertChannel);
        order.verify(alertsPoliciesApiMock).updateChannels(expected);
        order.verify(alertsChannelsApiMock).deleteFromPolicy(POLICY_ID, savedEmailChannel.getId());
        order.verify(alertsChannelsApiMock).delete(savedEmailChannel.getId());
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldNotRemoveUnsuedChannel_whenChannelBelongsToDifferentPolicy() {
        // given
        AlertsChannel emailChannelInPolicy = channelInPolicy(savedEmailChannel, POLICY_ID, POLICY_ID + 1);
        when(alertsChannelsApiMock.list()).thenReturn(ImmutableList.of(
                savedUserChannel,
                emailChannelInPolicy,
                channelInPolicy(savedSlackChannel, POLICY_ID)
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
                .channelIds(ImmutableSet.of(savedSlackChannel.getId()))
                .build();

        InOrder order = inOrder(alertsChannelsApiMock, alertsPoliciesApiMock);
        order.verify(alertsChannelsApiMock).list();
        order.verify(alertsPoliciesApiMock).updateChannels(expected);
        order.verify(alertsChannelsApiMock).deleteFromPolicy(POLICY_ID, savedEmailChannel.getId());
        order.verify(alertsChannelsApiMock, never()).delete(savedEmailChannel.getId());
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldRemoveUnusedPolicyChannel() {
        // given
        AlertsChannel emailChannelInPolicy = channelInPolicy(savedEmailChannel, POLICY_ID);
        when(alertsChannelsApiMock.list()).thenReturn(ImmutableList.of(
                savedUserChannel,
                emailChannelInPolicy,
                channelInPolicy(savedSlackChannel, POLICY_ID)
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
                .channelIds(ImmutableSet.of(savedSlackChannel.getId()))
                .build();

        InOrder order = inOrder(alertsChannelsApiMock, alertsPoliciesApiMock);
        order.verify(alertsChannelsApiMock).list();
        order.verify(alertsPoliciesApiMock).updateChannels(expected);
        order.verify(alertsChannelsApiMock).deleteFromPolicy(POLICY_ID, savedEmailChannel.getId());
        order.verify(alertsChannelsApiMock).delete(savedEmailChannel.getId());
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldThrowException_whenUserChannelDosNotExist() {
        // given
        PolicyConfiguration policyConfiguration = PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .channel(USER_CHANNEL)
                .build();

        // then - exception
        expectedException.expect(NewRelicSyncException.class);
        expectedException.expectMessage("Alerts channel with configuration");

        // when
        testee.sync(policyConfiguration);
    }

    @Test
    public void shouldNotCreateUserChannel() {
        // given
        when(alertsChannelsApiMock.list()).thenReturn(ImmutableList.of(savedUserChannel));
        PolicyConfiguration policyConfiguration = PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .channel(USER_CHANNEL)
                .build();

        AlertsPolicyChannels expected = AlertsPolicyChannels.builder()
                .policyId(POLICY.getId())
                .channelIds(ImmutableSet.of(savedUserChannel.getId()))
                .build();

        // when
        testee.sync(policyConfiguration);

        // then
        InOrder order = inOrder(alertsChannelsApiMock, alertsPoliciesApiMock);
        order.verify(alertsChannelsApiMock).list();
        order.verify(alertsPoliciesApiMock).updateChannels(expected);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldNotRemoveUnusedUserChannel() {
        // given
        AlertsChannel userChannelInPolicy = channelInPolicy(savedUserChannel, POLICY_ID);
        when(alertsChannelsApiMock.list()).thenReturn(ImmutableList.of(userChannelInPolicy));
        when(alertsChannelsApiMock.deleteFromPolicy(POLICY_ID, userChannelInPolicy.getId()))
                .thenReturn(userChannelInPolicy);

        // when
        PolicyConfiguration policyConfiguration = PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .build();
        testee.sync(policyConfiguration);

        // then
        AlertsPolicyChannels expected = AlertsPolicyChannels.builder()
                .policyId(POLICY_ID)
                .channelIds(emptySet())
                .build();

        InOrder order = inOrder(alertsChannelsApiMock, alertsPoliciesApiMock);
        order.verify(alertsChannelsApiMock).list();
        order.verify(alertsPoliciesApiMock).updateChannels(expected);
        order.verify(alertsChannelsApiMock).deleteFromPolicy(POLICY_ID, savedUserChannel.getId());
        order.verify(alertsChannelsApiMock, never()).delete(savedUserChannel.getId());
        order.verifyNoMoreInteractions();
    }

    private AlertsChannel createAlertChannel(Channel channel) {
        return createAlertChannel(null, channel);
    }

    private AlertsChannel createAlertChannel(Integer id, Channel channel) {
        return AlertsChannel.builder()
                .id(id)
                .name(channel.getChannelName())
                .type(channel.getTypeString())
                .configuration(channel.getChannelTypeSupport().generateAlertsChannelConfiguration(apiMock))
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
