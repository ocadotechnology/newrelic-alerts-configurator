package com.ocado.pandateam.newrelic.sync;

import com.google.common.collect.ImmutableList;
import com.ocado.pandateam.newrelic.api.AlertsChannelsApi;
import com.ocado.pandateam.newrelic.api.AlertsConditionsApi;
import com.ocado.pandateam.newrelic.api.AlertsExternalServiceConditionsApi;
import com.ocado.pandateam.newrelic.api.AlertsPoliciesApi;
import com.ocado.pandateam.newrelic.api.ApplicationsApi;
import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.model.channels.AlertChannel;
import com.ocado.pandateam.newrelic.api.model.channels.AlertChannelConfiguration;
import com.ocado.pandateam.newrelic.api.model.policies.AlertPolicy;
import com.ocado.pandateam.newrelic.sync.channel.Channel;
import com.ocado.pandateam.newrelic.sync.channel.EmailChannel;
import com.ocado.pandateam.newrelic.sync.channel.SlackChannel;
import com.ocado.pandateam.newrelic.sync.configuration.ChannelConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.PolicyConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChannelSynchronizerTest {

    @Mock
    private NewRelicApi apiMock;
    @Mock
    private ApplicationsApi applicationsApiMock;
    @Mock
    private AlertsChannelsApi alertsChannelsApiMock;
    @Mock
    private AlertsPoliciesApi alertsPoliciesApiMock;
    @Mock
    private AlertsConditionsApi alertsConditionsApiMock;
    @Mock
    private AlertsExternalServiceConditionsApi alertsExternalServiceConditionsApiMock;

    @Mock
    private ChannelConfiguration channelConfigurationMock;
    @Mock
    private PolicyConfiguration policyConfigurationMock;

    private ChannelSynchronizer testee;

    private static final String POLICY_NAME = "policyName";
    private static final AlertPolicy POLICY = AlertPolicy.builder().id(1).name(POLICY_NAME).build();

    private static final String EMAIL_CHANNEL_NAME = "emailChannel";
    private static final Channel EMAIL_CHANNEL = new EmailChannel(EMAIL_CHANNEL_NAME, "email", true);
    private static final Channel SLACK_CHANNEL = new SlackChannel("channel2", "slackUrl", "slackName");

    @Before
    public void setUp() {
        testee = new ChannelSynchronizer(apiMock, channelConfigurationMock);
        mockApi();
        mockValidChannelConfiguration();
        when(alertsChannelsApiMock.create(any(AlertChannel.class))).thenReturn(createEmailAlertChannel(10, "test"));
        when(alertsPoliciesApiMock.getByName(eq(POLICY_NAME))).thenReturn(Optional.of(POLICY));
    }

    @Test
    public void shouldRemoveDuplicatedChannels() throws NewRelicSyncException {
        // given
        when(alertsChannelsApiMock.list()).thenReturn(ImmutableList.of(createEmailAlertChannel(1, EMAIL_CHANNEL_NAME), createEmailAlertChannel(2, EMAIL_CHANNEL_NAME)));
        // when
        testee.sync();

        // then

    }

    private void mockApi() {
        when(apiMock.getApplicationsApi()).thenReturn(applicationsApiMock);
        when(apiMock.getAlertsChannelsApi()).thenReturn(alertsChannelsApiMock);
        when(apiMock.getAlertsPoliciesApi()).thenReturn(alertsPoliciesApiMock);
        when(apiMock.getAlertsConditionsApi()).thenReturn(alertsConditionsApiMock);
        when(apiMock.getAlertsExternalServiceConditionsApi()).thenReturn(alertsExternalServiceConditionsApiMock);
    }

    private void mockValidChannelConfiguration() {
        when(channelConfigurationMock.getPolicyName()).thenReturn(POLICY_NAME);
        when(channelConfigurationMock.getChannels()).thenReturn(ImmutableList.of(EMAIL_CHANNEL, SLACK_CHANNEL));
    }

    private static AlertChannel createEmailAlertChannel(int id, String name) {
        AlertChannelConfiguration config = AlertChannelConfiguration.builder()
                .recipients("recipents").includeJsonAttachment("true").build();
        return AlertChannel.builder().id(id).name(name).type("email").configuration(config).build();
    }
}
