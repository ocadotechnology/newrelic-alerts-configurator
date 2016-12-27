package com.ocado.panda.newrelic.sync;

import com.ocado.panda.newrelic.api.AlertsChannelsApi;
import com.ocado.panda.newrelic.api.AlertsConditionsApi;
import com.ocado.panda.newrelic.api.AlertsExternalServiceConditionsApi;
import com.ocado.panda.newrelic.api.AlertsPoliciesApi;
import com.ocado.panda.newrelic.api.ApplicationsApi;
import com.ocado.panda.newrelic.api.KeyTransactionsApi;
import com.ocado.panda.newrelic.api.NewRelicApi;
import com.ocado.panda.newrelic.api.ServersApi;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.when;

public abstract class AbstractSynchronizerTest {
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
    KeyTransactionsApi keyTransactionsApiMock;
    @Mock
    ServersApi serversApiMock;

    @Before
    public void mockApi() {
        when(apiMock.getApplicationsApi()).thenReturn(applicationsApiMock);
        when(apiMock.getAlertsChannelsApi()).thenReturn(alertsChannelsApiMock);
        when(apiMock.getAlertsPoliciesApi()).thenReturn(alertsPoliciesApiMock);
        when(apiMock.getAlertsConditionsApi()).thenReturn(alertsConditionsApiMock);
        when(apiMock.getAlertsExternalServiceConditionsApi()).thenReturn(alertsExternalServiceConditionsApiMock);
        when(apiMock.getKeyTransactionsApi()).thenReturn(keyTransactionsApiMock);
        when(apiMock.getServersApi()).thenReturn(serversApiMock);
    }
}
