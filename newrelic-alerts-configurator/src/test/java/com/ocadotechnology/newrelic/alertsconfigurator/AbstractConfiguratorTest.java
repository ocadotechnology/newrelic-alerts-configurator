package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.apiclient.*;
import com.ocadotechnology.newrelic.apiclient.model.conditions.AlertsCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.AlertsNrqlCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.external.AlertsExternalServiceCondition;
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
    PolicyItemApi<AlertsCondition> alertsConditionsApiMock;
    @Mock
    PolicyItemApi<AlertsNrqlCondition> alertsNrqlConditionsApiMock;
    @Mock
    PolicyItemApi<AlertsExternalServiceCondition> alertsExternalServiceConditionsApiMock;
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
        when(apiMock.getAlertsNrqlConditionsApi()).thenReturn(alertsNrqlConditionsApiMock);
        when(apiMock.getAlertsExternalServiceConditionsApi()).thenReturn(alertsExternalServiceConditionsApiMock);
        when(apiMock.getKeyTransactionsApi()).thenReturn(keyTransactionsApiMock);
        when(apiMock.getServersApi()).thenReturn(serversApiMock);
        when(apiMock.getUsersApi()).thenReturn(usersApiMock);
    }
}
