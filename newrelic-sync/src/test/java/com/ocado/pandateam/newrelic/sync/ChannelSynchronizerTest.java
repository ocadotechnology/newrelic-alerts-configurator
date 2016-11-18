package com.ocado.pandateam.newrelic.sync;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannel;
import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannelConfiguration;
import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannelLinks;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicyChannels;
import com.ocado.pandateam.newrelic.sync.configuration.PolicyConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.channel.Channel;
import com.ocado.pandateam.newrelic.sync.configuration.channel.ChannelUtils;
import com.ocado.pandateam.newrelic.sync.configuration.channel.EmailChannel;
import com.ocado.pandateam.newrelic.sync.configuration.channel.SlackChannel;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

public class ChannelSynchronizerTest extends AbstractSynchronizerTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

        private static final String POLICY_NAME = "policyName";
    private static final AlertsPolicy POLICY = AlertsPolicy.builder().id(42).name(POLICY_NAME).build();

    private static final String EMAIL_CHANNEL_NAME = "emailChannel";
    private static final String SLACK_CHANNEL_NAME = "slackChannel";
    private static final Channel EMAIL_CHANNEL = EmailChannel.builder().channelName(EMAIL_CHANNEL_NAME).emailAddress("recipents").build();
    private static final Channel SLACK_CHANNEL = SlackChannel.builder().channelName(SLACK_CHANNEL_NAME).slackUrl("url").build();

    private static final AlertsChannelConfiguration EMAIL_CHANNEL_CONFIG = ChannelUtils.generateAlertsChannelConfiguration(EMAIL_CHANNEL);
    private static final AlertsChannelConfiguration SLACK_CHANNEL_CONFIG = ChannelUtils.generateAlertsChannelConfiguration(SLACK_CHANNEL);
    private static final AlertsChannelConfiguration ALERT_CHANNEL_CONFIG = AlertsChannelConfiguration.builder().build();

    private static final AlertsChannel EMAIL_CHANNEL_CONFIG_MAPPED = createAlertChannel(EMAIL_CHANNEL_NAME, EMAIL_CHANNEL.getTypeString(), EMAIL_CHANNEL_CONFIG);
    private static final AlertsChannel SLACK_CHANNEL_CONFIG_MAPPED = createAlertChannel(SLACK_CHANNEL_NAME, SLACK_CHANNEL.getTypeString(), SLACK_CHANNEL_CONFIG);
    private static final AlertsChannel EMAIL_ALERT_CHANNEL_SAME = createAlertChannel(1, EMAIL_CHANNEL_NAME, "email", EMAIL_CHANNEL_CONFIG);
    private static final AlertsChannel EMAIL_ALERT_CHANNEL_SAMEINSTANCE = createAlertChannel(2, EMAIL_CHANNEL_NAME, "email", ALERT_CHANNEL_CONFIG);
    private static final AlertsChannel EMAIL_ALERT_CHANNEL_DIFFERENT = createAlertChannel(3, "different", "email", ALERT_CHANNEL_CONFIG);
    private static final AlertsChannel SLACK_ALERT_CHANNEL_SAME = createAlertChannel(4, SLACK_CHANNEL_NAME, "slack", SLACK_CHANNEL_CONFIG);

    private ChannelSynchronizer testee;
    private static final PolicyConfiguration CONFIGURATION = createConfiguration();

    @Before
    public void setUp() {
        testee = new ChannelSynchronizer(apiMock);

        when(alertsChannelsApiMock.create(EMAIL_CHANNEL_CONFIG_MAPPED)).thenReturn(EMAIL_ALERT_CHANNEL_SAME);
        when(alertsChannelsApiMock.create(SLACK_CHANNEL_CONFIG_MAPPED)).thenReturn(SLACK_ALERT_CHANNEL_SAME);
        when(alertsPoliciesApiMock.getByName(POLICY_NAME)).thenReturn(Optional.of(POLICY));
    }

    @Test
    public void shouldThrowException_whenPolicyDoesNotExist() {
        // given
        when(alertsPoliciesApiMock.getByName(POLICY_NAME)).thenReturn(Optional.empty());

        // then - exception
        expectedException.expect(NewRelicSyncException.class);
        expectedException.expectMessage(format("Policy %s does not exist", POLICY_NAME));

        // when
        testee.sync(CONFIGURATION);
    }

    @Test
    public void shouldDoNothing_whenNoChannelsInConfiguration() {
        // given
        PolicyConfiguration config = PolicyConfiguration.builder()
            .policyName(POLICY_NAME)
            .build();

        // when
        testee.sync(config);

        // then
        InOrder order = inOrder(alertsChannelsApiMock);
        order.verify(alertsChannelsApiMock).list();
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldAddChannels() {
        // given

        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsChannelsApiMock);
        verifyChannelsAdded(order);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldRemoveSameInstanceChannelsAndCreateNewOne_whenChannelUpdated() {
        // given
        when(alertsChannelsApiMock.list()).thenReturn(ImmutableList.of(EMAIL_ALERT_CHANNEL_SAMEINSTANCE, EMAIL_ALERT_CHANNEL_DIFFERENT));
        when(alertsChannelsApiMock.deleteFromPolicy(POLICY.getId(), EMAIL_ALERT_CHANNEL_SAMEINSTANCE.getId())).thenReturn(EMAIL_ALERT_CHANNEL_SAMEINSTANCE);
        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsChannelsApiMock);
        order.verify(alertsChannelsApiMock).list();
        order.verify(alertsChannelsApiMock).create(EMAIL_CHANNEL_CONFIG_MAPPED);
        order.verify(alertsChannelsApiMock).create(SLACK_CHANNEL_CONFIG_MAPPED);
        order.verify(alertsChannelsApiMock).deleteFromPolicy(POLICY.getId(), EMAIL_ALERT_CHANNEL_SAMEINSTANCE.getId());
        order.verify(alertsChannelsApiMock).delete(EMAIL_ALERT_CHANNEL_SAMEINSTANCE.getId());
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldNotRemoveChannel_whenChannelNotUpdated() {
        // given
        when(alertsChannelsApiMock.list()).thenReturn(ImmutableList.of(EMAIL_ALERT_CHANNEL_SAME, EMAIL_ALERT_CHANNEL_DIFFERENT));
        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsChannelsApiMock);
        order.verify(alertsChannelsApiMock).list();
        order.verify(alertsChannelsApiMock).create(SLACK_CHANNEL_CONFIG_MAPPED);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldUpdateAlertPolicyChannels() {
        // given
        AlertsChannel channelWithOnlyCurrentPolicyId = createAlertChannel(6, "different", "email", ALERT_CHANNEL_CONFIG, Lists.newArrayList(POLICY.getId(), 100));
        when(alertsChannelsApiMock.list()).thenReturn(ImmutableList.of(channelWithOnlyCurrentPolicyId));
        when(alertsChannelsApiMock.deleteFromPolicy(POLICY.getId(), channelWithOnlyCurrentPolicyId.getId())).thenReturn(channelWithOnlyCurrentPolicyId);

        AlertsPolicyChannels expected = AlertsPolicyChannels.builder()
            .policyId(POLICY.getId())
            .channelIds(ImmutableSet.of(EMAIL_ALERT_CHANNEL_SAME.getId(), SLACK_ALERT_CHANNEL_SAME.getId()))
            .build();

        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsChannelsApiMock, alertsPoliciesApiMock);
        verifyChannelsAdded(order);
        order.verify(alertsPoliciesApiMock).updateChannels(expected);
        order.verify(alertsChannelsApiMock).deleteFromPolicy(POLICY.getId(), channelWithOnlyCurrentPolicyId.getId());
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldRemoveUnusedPolicyChannel() {
        // given
        AlertsChannel channelWithOnlyCurrentPolicyId = createAlertChannel(6, "different", "email", ALERT_CHANNEL_CONFIG, Lists.newArrayList(POLICY.getId()));
        when(alertsChannelsApiMock.list()).thenReturn(ImmutableList.of(channelWithOnlyCurrentPolicyId));
        when(alertsChannelsApiMock.deleteFromPolicy(POLICY.getId(), channelWithOnlyCurrentPolicyId.getId())).thenReturn(channelWithOnlyCurrentPolicyId);

        AlertsPolicyChannels expected = AlertsPolicyChannels.builder()
            .policyId(POLICY.getId())
            .channelIds(ImmutableSet.of(EMAIL_ALERT_CHANNEL_SAME.getId(), SLACK_ALERT_CHANNEL_SAME.getId()))
            .build();

        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsChannelsApiMock, alertsPoliciesApiMock);
        verifyChannelsAdded(order);
        order.verify(alertsPoliciesApiMock).updateChannels(expected);
        order.verify(alertsChannelsApiMock).deleteFromPolicy(POLICY.getId(), channelWithOnlyCurrentPolicyId.getId());
        order.verify(alertsChannelsApiMock).delete(channelWithOnlyCurrentPolicyId.getId());
        order.verifyNoMoreInteractions();
    }

    private void verifyChannelsAdded(InOrder order) {
        order.verify(alertsChannelsApiMock).list();
        order.verify(alertsChannelsApiMock).create(EMAIL_CHANNEL_CONFIG_MAPPED);
        order.verify(alertsChannelsApiMock).create(SLACK_CHANNEL_CONFIG_MAPPED);
    }

    private static AlertsChannel createAlertChannel(String name, String type, AlertsChannelConfiguration config) {
        return AlertsChannel.builder().name(name).type(type).configuration(config).build();
    }

    private static AlertsChannel createAlertChannel(int id, String name, String type, AlertsChannelConfiguration config) {
        AlertsChannelLinks alertChannelLinks = new AlertsChannelLinks(Collections.emptyList());
        return AlertsChannel.builder().id(id).name(name).type(type).configuration(config).links(alertChannelLinks).build();
    }

    private static AlertsChannel createAlertChannel(int id, String name, String type, AlertsChannelConfiguration config, List<Integer> policyIds) {
        AlertsChannelLinks alertChannelLinks = new AlertsChannelLinks(policyIds);
        return AlertsChannel.builder().id(id).name(name).type(type).configuration(config).links(alertChannelLinks).build();
    }

    private static PolicyConfiguration createConfiguration() {
        return PolicyConfiguration.builder()
            .policyName(POLICY_NAME)
            .channel(EMAIL_CHANNEL)
            .channel(SLACK_CHANNEL)
            .build();
    }
}
