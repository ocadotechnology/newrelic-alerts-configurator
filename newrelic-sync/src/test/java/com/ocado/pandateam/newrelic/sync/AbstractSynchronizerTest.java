package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.AlertsChannelsApi;
import com.ocado.pandateam.newrelic.api.AlertsConditionsApi;
import com.ocado.pandateam.newrelic.api.AlertsExternalServiceConditionsApi;
import com.ocado.pandateam.newrelic.api.AlertsPoliciesApi;
import com.ocado.pandateam.newrelic.api.ApplicationsApi;
import com.ocado.pandateam.newrelic.api.NewRelicApi;
import org.junit.Before;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

class AbstractSynchronizerTest {
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
    }
}
