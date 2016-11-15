package com.ocado.pandateam.newrelic.sync;

import com.google.common.collect.ImmutableList;
import com.ocado.pandateam.newrelic.api.AlertsChannelsApi;
import com.ocado.pandateam.newrelic.api.AlertsConditionsApi;
import com.ocado.pandateam.newrelic.api.AlertsExternalServiceConditionsApi;
import com.ocado.pandateam.newrelic.api.AlertsPoliciesApi;
import com.ocado.pandateam.newrelic.api.ApplicationsApi;
import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.model.applications.Application;
import com.ocado.pandateam.newrelic.api.model.channels.AlertChannel;
import com.ocado.pandateam.newrelic.api.model.channels.AlertChannelConfiguration;
import com.ocado.pandateam.newrelic.sync.channel.Channel;
import com.ocado.pandateam.newrelic.sync.channel.EmailChannel;
import com.ocado.pandateam.newrelic.sync.channel.SlackChannel;
import com.ocado.pandateam.newrelic.sync.configuration.ChannelConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.Configuration;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SynchronizerTest {

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
    private Configuration configurationMock;

    private Synchronizer testee;

    private static final String APPLICATION_NAME = "appName";
    private static final String POLICY_NAME = "policyName";
    private static final float APP_APDEX_THRESHOLD = 1f;
    private static final float USER_APDEX_THRESHOLD = 1f;

    private static final String EMAIL_CHANNEL_NAME = "emailChannel";
    private static final Channel EMAIL_CHANNEL = new EmailChannel(EMAIL_CHANNEL_NAME, "email", true);
    private static final Channel SLACK_CHANNEL = new SlackChannel("channel2", "slackUrl", "slackName");

    @Before
    public void setUp() {
        testee = new Synchronizer(apiMock, configurationMock, channelConfigurationMock);
        mockApi();
        mockValidConfiguration();
        mockValidChannelConfiguration();
        when(alertsChannelsApiMock.create(any(AlertChannel.class))).thenReturn(createEmailAlertChannel(10, "test"));
        when(applicationsApiMock.getByName(anyString())).thenReturn(Optional.of(Application.builder().id(1).build()));
    }

    @Test
    public void aTest() {

    }

    @Test
    @Ignore
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

    private void mockValidConfiguration() {
        when(configurationMock.getApplicationName()).thenReturn(APPLICATION_NAME);
        when(configurationMock.getPolicyName()).thenReturn(POLICY_NAME);
        when(configurationMock.getAppApdexThreshold()).thenReturn(APP_APDEX_THRESHOLD);
        when(configurationMock.getUserApdexThreshold()).thenReturn(USER_APDEX_THRESHOLD);
    }

    private void mockValidChannelConfiguration() {
        when(channelConfigurationMock.getChannels()).thenReturn(ImmutableList.of(EMAIL_CHANNEL, SLACK_CHANNEL));
    }

    private static AlertChannel createEmailAlertChannel(int id, String name) {
        AlertChannelConfiguration config = AlertChannelConfiguration.builder()
                .recipients("recipents").includeJsonAttachment("true").build();
        return AlertChannel.builder().id(id).name(name).type("email").configuration(config).build();
    }
}
