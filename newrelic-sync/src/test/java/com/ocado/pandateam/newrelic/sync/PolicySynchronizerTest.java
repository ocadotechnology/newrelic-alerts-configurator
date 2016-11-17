package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.sync.configuration.PolicyConfiguration;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class PolicySynchronizerTest extends AbstractSynchronizerTest {
    private PolicySynchronizer testee;
    private PolicyConfiguration policyConfiguration = createConfiguration();

    private static final String POLICY_NAME = "policyName";
    private static final PolicyConfiguration.IncidentPreference INCIDENT_PREFERENCE = PolicyConfiguration.IncidentPreference.PER_CONDITION;
    private static final AlertsPolicy ALERT_POLICY_SAME = createAlertPolicy(1, INCIDENT_PREFERENCE);
    private static final AlertsPolicy ALERT_POLICY_DIFFERENT = createAlertPolicy(2, PolicyConfiguration.IncidentPreference.PER_POLICY);

    @Before
    public void setUp() {
        testee = new PolicySynchronizer(apiMock, policyConfiguration);
    }

    @Test
    public void shouldCreateNewPolicy_whenPolicyDoesNotExist() throws NewRelicSyncException {
        // given
        when(alertsPoliciesApiMock.getByName(eq(POLICY_NAME))).thenReturn(Optional.empty());
        AlertsPolicy expectedPolicy = AlertsPolicy.builder().name(POLICY_NAME).incidentPreference(INCIDENT_PREFERENCE.name()).build();

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
        AlertsPolicy expectedPolicy = AlertsPolicy.builder().name(POLICY_NAME).incidentPreference(INCIDENT_PREFERENCE.name()).build();

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

    private static AlertsPolicy createAlertPolicy(int id, PolicyConfiguration.IncidentPreference incidentPreference) {
        return AlertsPolicy.builder().id(id).name(POLICY_NAME).incidentPreference(incidentPreference.name()).build();
    }

    private static PolicyConfiguration createConfiguration() {
        return PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .incidentPreference(INCIDENT_PREFERENCE)
                .build();
    }
}
