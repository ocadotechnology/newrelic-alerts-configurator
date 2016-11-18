package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.sync.configuration.PolicyConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.Optional;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

public class PolicySynchronizerTest extends AbstractSynchronizerTest {
    private static final String POLICY_NAME = "policyName";
    private static final PolicyConfiguration.IncidentPreference INCIDENT_PREFERENCE = PolicyConfiguration.IncidentPreference.PER_CONDITION;
    private static final AlertsPolicy ALERT_POLICY_SAME = createAlertPolicy(1, INCIDENT_PREFERENCE);
    private static final AlertsPolicy ALERT_POLICY_DIFFERENT = createAlertPolicy(2, PolicyConfiguration.IncidentPreference.PER_POLICY);

    private PolicySynchronizer testee;
    private static final PolicyConfiguration CONFIGURATION = createConfiguration();

    @Before
    public void setUp() {
        testee = new PolicySynchronizer(apiMock);
    }

    @Test
    public void shouldCreateNewPolicy_whenPolicyDoesNotExist() {
        // given
        when(alertsPoliciesApiMock.getByName(POLICY_NAME)).thenReturn(Optional.empty());
        AlertsPolicy expectedPolicy = AlertsPolicy.builder().name(POLICY_NAME).incidentPreference(INCIDENT_PREFERENCE.name()).build();

        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsPoliciesApiMock);
        order.verify(alertsPoliciesApiMock).getByName(POLICY_NAME);
        order.verify(alertsPoliciesApiMock).create(expectedPolicy);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldDeleteAndCreateNewPolicy_whenPolicyUpdated() {
        // given
        when(alertsPoliciesApiMock.getByName(POLICY_NAME)).thenReturn(Optional.of(ALERT_POLICY_DIFFERENT));
        AlertsPolicy expectedPolicy = AlertsPolicy.builder().name(POLICY_NAME).incidentPreference(INCIDENT_PREFERENCE.name()).build();

        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsPoliciesApiMock);
        order.verify(alertsPoliciesApiMock).getByName(POLICY_NAME);
        order.verify(alertsPoliciesApiMock).delete(ALERT_POLICY_DIFFERENT.getId());
        order.verify(alertsPoliciesApiMock).create(expectedPolicy);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldDoNothing_whenPolicyNotUpdated() {
        // given
        when(alertsPoliciesApiMock.getByName(POLICY_NAME)).thenReturn(Optional.of(ALERT_POLICY_SAME));

        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsPoliciesApiMock);
        order.verify(alertsPoliciesApiMock).getByName(POLICY_NAME);
        order.verifyNoMoreInteractions();
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
