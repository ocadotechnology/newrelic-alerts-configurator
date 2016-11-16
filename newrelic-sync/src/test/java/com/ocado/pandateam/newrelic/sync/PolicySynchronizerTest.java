package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.model.policies.AlertPolicy;
import com.ocado.pandateam.newrelic.sync.configuration.PolicyConfiguration;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PolicySynchronizerTest extends AbstractSynchronizerTest {
    @Mock
    private PolicyConfiguration policyConfigurationMock;

    private PolicySynchronizer testee;

    private static final String POLICY_NAME = "policyName";
    private static final PolicyConfiguration.IncidentPreference INCIDENT_PREFERENCE = PolicyConfiguration.IncidentPreference.PER_CONDITION;
    private static final AlertPolicy ALERT_POLICY_SAME = createAlertPolicy(1, INCIDENT_PREFERENCE);
    private static final AlertPolicy ALERT_POLICY_DIFFERENT = createAlertPolicy(2, PolicyConfiguration.IncidentPreference.PER_POLICY);

    @Before
    public void setUp() {
        testee = new PolicySynchronizer(apiMock, policyConfigurationMock);
        mockValidPolicyConfiguration();
    }

    @Test
    public void shouldCreateNewPolicy_whenPolicyDoesNotExist() throws NewRelicSyncException {
        // given
        when(alertsPoliciesApiMock.getByName(eq(POLICY_NAME))).thenReturn(Optional.empty());
        AlertPolicy expectedPolicy = AlertPolicy.builder().name(POLICY_NAME).incidentPreference(INCIDENT_PREFERENCE.name()).build();

        // when
        testee.sync();

        // then
        verify(alertsPoliciesApiMock).getByName(eq(POLICY_NAME));
        verify(alertsPoliciesApiMock).create(eq(expectedPolicy));
        verifyNoMoreInteractions(alertsPoliciesApiMock);
    }

    @Test
    public void shouldDeleteAndCreateNewPolicy_whenPolicyUpdated() throws NewRelicSyncException {
        // given
        when(alertsPoliciesApiMock.getByName(eq(POLICY_NAME))).thenReturn(Optional.of(ALERT_POLICY_DIFFERENT));
        AlertPolicy expectedPolicy = AlertPolicy.builder().name(POLICY_NAME).incidentPreference(INCIDENT_PREFERENCE.name()).build();

        // when
        testee.sync();

        // then
        verify(alertsPoliciesApiMock).getByName(eq(POLICY_NAME));
        verify(alertsPoliciesApiMock).delete(ALERT_POLICY_DIFFERENT.getId());
        verify(alertsPoliciesApiMock).create(eq(expectedPolicy));
        verifyNoMoreInteractions(alertsPoliciesApiMock);
    }

    @Test
    public void shouldDoNothing_whenPolicyNotUpdated() throws NewRelicSyncException {
        // given
        when(alertsPoliciesApiMock.getByName(eq(POLICY_NAME))).thenReturn(Optional.of(ALERT_POLICY_SAME));

        // when
        testee.sync();

        // then
        verify(alertsPoliciesApiMock).getByName(eq(POLICY_NAME));
        verifyNoMoreInteractions(alertsPoliciesApiMock);
    }

    private void mockValidPolicyConfiguration() {
        when(policyConfigurationMock.getPolicyName()).thenReturn(POLICY_NAME);
        when(policyConfigurationMock.getIncidentPreference()).thenReturn(INCIDENT_PREFERENCE);
    }

    private static AlertPolicy createAlertPolicy(int id, PolicyConfiguration.IncidentPreference incidentPreference) {
        return AlertPolicy.builder().id(id).name(POLICY_NAME).incidentPreference(incidentPreference.name()).build();
    }
}
