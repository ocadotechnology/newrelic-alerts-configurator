package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.AlertsChannelsApi;
import com.ocado.pandateam.newrelic.api.AlertsConditionsApi;
import com.ocado.pandateam.newrelic.api.AlertsExternalServiceConditionsApi;
import com.ocado.pandateam.newrelic.api.AlertsPoliciesApi;
import com.ocado.pandateam.newrelic.api.ApplicationsApi;
import com.ocado.pandateam.newrelic.api.KeyTransactionsApi;
import com.ocado.pandateam.newrelic.api.NewRelicApi;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
abstract class AbstractSynchronizerTest {
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

    @Before
    public void setUpApi() {
        mockApi();
    }

    private void mockApi() {
        when(apiMock.getApplicationsApi()).thenReturn(applicationsApiMock);
        when(apiMock.getAlertsChannelsApi()).thenReturn(alertsChannelsApiMock);
        when(apiMock.getAlertsPoliciesApi()).thenReturn(alertsPoliciesApiMock);
        when(apiMock.getAlertsConditionsApi()).thenReturn(alertsConditionsApiMock);
        when(apiMock.getAlertsExternalServiceConditionsApi()).thenReturn(alertsExternalServiceConditionsApiMock);
        when(apiMock.getKeyTransactionsApi()).thenReturn(keyTransactionsApiMock);
    }
}
