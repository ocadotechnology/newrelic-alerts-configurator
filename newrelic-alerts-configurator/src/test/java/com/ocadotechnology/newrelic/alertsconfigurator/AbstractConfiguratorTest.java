package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.apiclient.AlertsChannelsApi;
import com.ocadotechnology.newrelic.apiclient.AlertsConditionsApi;
import com.ocadotechnology.newrelic.apiclient.AlertsExternalServiceConditionsApi;
import com.ocadotechnology.newrelic.apiclient.AlertsNrqlConditionsApi;
import com.ocadotechnology.newrelic.apiclient.AlertsPoliciesApi;
import com.ocadotechnology.newrelic.apiclient.ApplicationsApi;
import com.ocadotechnology.newrelic.apiclient.KeyTransactionsApi;
import com.ocadotechnology.newrelic.apiclient.NewRelicApi;
import com.ocadotechnology.newrelic.apiclient.ServersApi;
import com.ocadotechnology.newrelic.apiclient.UsersApi;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.when;

public abstract class AbstractConfiguratorTest {
    @Rule
    public final MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    NewRelicApi apiMock;
    @Mock
    ApplicationsApi applicationsApiMock;
    @Mock
    AlertsChannelsApi alertsChannelsApiMock;
    @Mock
    AlertsPoliciesApi alertsPoliciesApiMock;
    @Mock
    AlertsConditionsApi alertsConditionsApiMock;
    @Mock
    AlertsExternalServiceConditionsApi alertsExternalServiceConditionsApiMock;
    @Mock
    AlertsNrqlConditionsApi alertsNrqlConditionsApiMock;
    @Mock
    KeyTransactionsApi keyTransactionsApiMock;
    @Mock
    UsersApi usersApiMock;
    @Mock
    ServersApi serversApiMock;

    @Before
    public void mockApi() {
        when(apiMock.getApplicationsApi()).thenReturn(applicationsApiMock);
        when(apiMock.getAlertsChannelsApi()).thenReturn(alertsChannelsApiMock);
        when(apiMock.getAlertsPoliciesApi()).thenReturn(alertsPoliciesApiMock);
        when(apiMock.getAlertsConditionsApi()).thenReturn(alertsConditionsApiMock);
        when(apiMock.getAlertsExternalServiceConditionsApi()).thenReturn(alertsExternalServiceConditionsApiMock);
        when(apiMock.getAlertsNrqlConditionsApi()).thenReturn(alertsNrqlConditionsApiMock);
        when(apiMock.getKeyTransactionsApi()).thenReturn(keyTransactionsApiMock);
        when(apiMock.getServersApi()).thenReturn(serversApiMock);
        when(apiMock.getUsersApi()).thenReturn(usersApiMock);
    }
}
