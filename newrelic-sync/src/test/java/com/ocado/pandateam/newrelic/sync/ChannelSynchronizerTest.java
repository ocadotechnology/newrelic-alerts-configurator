package com.ocado.pandateam.newrelic.sync;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannel;
import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannelConfiguration;
import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannelLinks;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicyChannels;
import com.ocado.pandateam.newrelic.sync.configuration.ChannelConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.channel.Channel;
import com.ocado.pandateam.newrelic.sync.configuration.channel.EmailChannel;
import com.ocado.pandateam.newrelic.sync.configuration.channel.SlackChannel;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class ChannelSynchronizerTest extends AbstractSynchronizerTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private ChannelSynchronizer testee;
    private ChannelConfiguration channelConfiguration = createConfiguration();

    private static final String POLICY_NAME = "policyName";
    private static final AlertsPolicy POLICY = AlertsPolicy.builder().id(42).name(POLICY_NAME).build();

    private static final String EMAIL_CHANNEL_NAME = "emailChannel";
    private static final String SLACK_CHANNEL_NAME = "slackChannel";
    private static final Channel EMAIL_CHANNEL = EmailChannel.builder().channelName(EMAIL_CHANNEL_NAME).emailAddress("recipents").build();
    private static final Channel SLACK_CHANNEL = SlackChannel.builder().channelName(SLACK_CHANNEL_NAME).slackUrl("url").build();

    private static final AlertsChannelConfiguration EMAIL_CHANNEL_CONFIG = EMAIL_CHANNEL.getAlertChannelConfiguration();
    private static final AlertsChannelConfiguration SLACK_CHANNEL_CONFIG = SLACK_CHANNEL.getAlertChannelConfiguration();
    private static final AlertsChannelConfiguration ALERT_CHANNEL_CONFIG = AlertsChannelConfiguration.builder().build();

    private static final AlertsChannel EMAIL_CHANNEL_CONFIG_MAPPED = createAlertChannel(EMAIL_CHANNEL_NAME, EMAIL_CHANNEL.getType(), EMAIL_CHANNEL.getAlertChannelConfiguration());
    private static final AlertsChannel SLACK_CHANNEL_CONFIG_MAPPED = createAlertChannel(SLACK_CHANNEL_NAME, SLACK_CHANNEL.getType(), SLACK_CHANNEL.getAlertChannelConfiguration());
    private static final AlertsChannel EMAIL_ALERT_CHANNEL_SAME = createAlertChannel(1, EMAIL_CHANNEL_NAME, "email", EMAIL_CHANNEL_CONFIG);
    private static final AlertsChannel EMAIL_ALERT_CHANNEL_SAMEINSTANCE = createAlertChannel(2, EMAIL_CHANNEL_NAME, "email", ALERT_CHANNEL_CONFIG);
    private static final AlertsChannel EMAIL_ALERT_CHANNEL_DIFFERENT = createAlertChannel(3, "different", "email", ALERT_CHANNEL_CONFIG);
    private static final AlertsChannel SLACK_ALERT_CHANNEL_SAME = createAlertChannel(4, SLACK_CHANNEL_NAME, "slack", SLACK_CHANNEL_CONFIG);

    @Before
    public void setUp() {
        testee = new ChannelSynchronizer(apiMock, channelConfiguration);

        when(alertsChannelsApiMock.create(eq(EMAIL_CHANNEL_CONFIG_MAPPED))).thenReturn(EMAIL_ALERT_CHANNEL_SAME);
        when(alertsChannelsApiMock.create(eq(SLACK_CHANNEL_CONFIG_MAPPED))).thenReturn(SLACK_ALERT_CHANNEL_SAME);
        when(alertsPoliciesApiMock.getByName(eq(POLICY_NAME))).thenReturn(Optional.of(POLICY));
    }

    @Test
    public void shouldThrowException_whenPolicyDoesNotExist() throws NewRelicSyncException {
        // given
        when(alertsPoliciesApiMock.getByName(eq(POLICY_NAME))).thenReturn(Optional.empty());

        // then - exception
        expectedException.expect(NewRelicSyncException.class);

        // when
        testee.sync();
    }

    @Test
    public void shouldAddChannels() {
        // given

        // when
        testee.sync();

        // then
        verifyChannelsAdded();
        verifyNoMoreInteractions(alertsChannelsApiMock);
    }

    @Test
    public void shouldRemoveSameInstanceChannelsAndCreateNewOne_whenChannelUpdated() throws NewRelicSyncException {
        // given
        when(alertsChannelsApiMock.list()).thenReturn(ImmutableList.of(EMAIL_ALERT_CHANNEL_SAMEINSTANCE, EMAIL_ALERT_CHANNEL_DIFFERENT));
        // when
        testee.sync();

        // then
        verify(alertsChannelsApiMock).list();
        verify(alertsChannelsApiMock).delete(eq(EMAIL_ALERT_CHANNEL_SAMEINSTANCE.getId()));
        verify(alertsChannelsApiMock).create(eq(EMAIL_CHANNEL_CONFIG_MAPPED));
        verify(alertsChannelsApiMock).create(eq(SLACK_CHANNEL_CONFIG_MAPPED));
        verifyNoMoreInteractions(alertsChannelsApiMock);
    }

    @Test
    public void shouldNotRemoveChannel_whenChannelNotUpdated() throws NewRelicSyncException {
        // given
        when(alertsChannelsApiMock.list()).thenReturn(ImmutableList.of(EMAIL_ALERT_CHANNEL_SAME, EMAIL_ALERT_CHANNEL_DIFFERENT));
        // when
        testee.sync();

        // then
        verify(alertsChannelsApiMock).list();
        verify(alertsChannelsApiMock).create(eq(SLACK_CHANNEL_CONFIG_MAPPED));
        verifyNoMoreInteractions(alertsChannelsApiMock);
    }

    @Test
    public void shouldUpdateAlertPolicyChannels() throws NewRelicSyncException {
        // given
        AlertsChannel channelWithOnlyCurrentPolicyId = createAlertChannel(6, "different", "email", ALERT_CHANNEL_CONFIG, Lists.newArrayList(POLICY.getId(), 100));
        when(alertsChannelsApiMock.list()).thenReturn(ImmutableList.of(channelWithOnlyCurrentPolicyId));
        when(alertsChannelsApiMock.deleteFromPolicy(POLICY.getId(), channelWithOnlyCurrentPolicyId.getId())).thenReturn(channelWithOnlyCurrentPolicyId);

        AlertsPolicyChannels expected = AlertsPolicyChannels.builder()
            .policyId(POLICY.getId())
            .channelIds(ImmutableList.of(EMAIL_ALERT_CHANNEL_SAME.getId(), SLACK_ALERT_CHANNEL_SAME.getId()))
            .build();

        // when
        testee.sync();

        // then
        verifyChannelsAdded();
        verify(alertsPoliciesApiMock).updateChannels(eq(expected));
        verify(alertsChannelsApiMock).deleteFromPolicy(POLICY.getId(), channelWithOnlyCurrentPolicyId.getId());
        verifyNoMoreInteractions(alertsChannelsApiMock);
    }

    @Test
    public void shouldRemoveUnusedPolicyChannel() throws NewRelicSyncException {
        // given
        AlertsChannel channelWithOnlyCurrentPolicyId = createAlertChannel(6, "different", "email", ALERT_CHANNEL_CONFIG, Lists.newArrayList(POLICY.getId()));
        when(alertsChannelsApiMock.list()).thenReturn(ImmutableList.of(channelWithOnlyCurrentPolicyId));
        when(alertsChannelsApiMock.deleteFromPolicy(POLICY.getId(), channelWithOnlyCurrentPolicyId.getId())).thenReturn(channelWithOnlyCurrentPolicyId);

        AlertsPolicyChannels expected = AlertsPolicyChannels.builder()
            .policyId(POLICY.getId())
            .channelIds(ImmutableList.of(EMAIL_ALERT_CHANNEL_SAME.getId(), SLACK_ALERT_CHANNEL_SAME.getId()))
            .build();

        // when
        testee.sync();

        // then
        verifyChannelsAdded();
        verify(alertsPoliciesApiMock).updateChannels(eq(expected));
        verify(alertsChannelsApiMock).deleteFromPolicy(POLICY.getId(), channelWithOnlyCurrentPolicyId.getId());
        verify(alertsChannelsApiMock).delete(channelWithOnlyCurrentPolicyId.getId());
        verifyNoMoreInteractions(alertsChannelsApiMock);
    }

    private void verifyChannelsAdded() {
        verify(alertsChannelsApiMock).list();
        verify(alertsChannelsApiMock).create(eq(EMAIL_CHANNEL_CONFIG_MAPPED));
        verify(alertsChannelsApiMock).create(eq(SLACK_CHANNEL_CONFIG_MAPPED));
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

    private ChannelConfiguration createConfiguration() {
        return ChannelConfiguration.builder()
            .policyName(POLICY_NAME)
            .channels(ImmutableList.of(EMAIL_CHANNEL, SLACK_CHANNEL))
            .build();
    }
}
