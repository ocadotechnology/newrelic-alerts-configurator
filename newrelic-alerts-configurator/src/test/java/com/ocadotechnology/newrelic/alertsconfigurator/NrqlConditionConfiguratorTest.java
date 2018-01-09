package com.ocadotechnology.newrelic.alertsconfigurator;

import com.google.common.collect.ImmutableList;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql.NrqlCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql.NrqlConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql.ValueFunction;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.NrqlDurationTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.NrqlTermsConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.OperatorTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.PriorityTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TimeFunctionTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.exception.NewRelicSyncException;
import com.ocadotechnology.newrelic.apiclient.model.conditions.Terms;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.AlertsNrqlCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.Nrql;
import com.ocadotechnology.newrelic.apiclient.model.policies.AlertsPolicy;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;
import org.mockito.InjectMocks;

import java.util.Collections;
import java.util.Optional;

import static java.lang.String.format;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

public class NrqlConditionConfiguratorTest extends AbstractConfiguratorTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private static final String POLICY_NAME = "policyName";
    private static final AlertsPolicy POLICY = AlertsPolicy.builder().id(42).name(POLICY_NAME).build();

    private static final String CONDITION_NAME = "conditionName";
    private static final boolean ENABLED = true;
    private static final NrqlTermsConfiguration TERMS_CONFIGURATION = createTermsConfiguration().build();
    private static final ValueFunction VALUE_FUNCTION = ValueFunction.SINGLE_VALUE;
    private static final NrqlConfiguration NRQL_CONFIGURATION = createNrqlConfiguration();

    private static final NrqlCondition NRQL_CONDITION = createNrqlCondition(CONDITION_NAME);
    private static final AlertsNrqlCondition ALERTS_CONDITION_SAME = createConditionBuilder().id(1).build();
    private static final AlertsNrqlCondition ALERTS_CONDITION_FROM_CONFIG = createConditionBuilder().build();
    private static final AlertsNrqlCondition ALERTS_CONDITION_UPDATED = createConditionBuilder().id(2).enabled(!ENABLED).build();
    private static final AlertsNrqlCondition ALERTS_CONDITION_DIFFERENT = createConditionBuilder().id(3).name("different").build();

    private static final PolicyConfiguration CONFIGURATION = createConfiguration();

    @InjectMocks
    private NrqlConditionConfigurator testee;

    @Before
    public void setUp() {
        when(alertsPoliciesApiMock.getByName(POLICY_NAME)).thenReturn(Optional.of(POLICY));
    }

    @Test
    public void shouldThrowException_whenPolicyDoesNotExist() {
        // given
        when(alertsPoliciesApiMock.getByName(POLICY_NAME)).thenReturn(Optional.empty());

        // then - exception
        expectedException.expect(NewRelicSyncException.class);
        expectedException.expectMessage(format("Policy %s does not exist", POLICY_NAME));

        // when
        testee.sync(CONFIGURATION);
    }

    @Test
    public void shouldDoNothing_whenNullConditionsInConfiguration() {
        // given
        PolicyConfiguration config = PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
                .build();

        // when
        testee.sync(config);

        // then
        InOrder order = inOrder(alertsNrqlConditionsApiMock);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldDoNothing_whenEmptyConditionsInConfiguration() {
        // given
        PolicyConfiguration config = PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
                .nrqlConditions(Collections.emptyList())
                .build();

        // when
        testee.sync(config);

        // then
        InOrder order = inOrder(alertsNrqlConditionsApiMock);
        order.verify(alertsNrqlConditionsApiMock).list(POLICY.getId());
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldCreateCondition() {
        // given
        when(alertsNrqlConditionsApiMock.list(POLICY.getId())).thenReturn(ImmutableList.of());
        when(alertsNrqlConditionsApiMock.create(POLICY.getId(), ALERTS_CONDITION_FROM_CONFIG)).thenReturn(ALERTS_CONDITION_SAME);

        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsNrqlConditionsApiMock);
        order.verify(alertsNrqlConditionsApiMock).list(POLICY.getId());
        order.verify(alertsNrqlConditionsApiMock).create(POLICY.getId(), ALERTS_CONDITION_FROM_CONFIG);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldUpdateCondition() {
        // given
        when(alertsNrqlConditionsApiMock.list(POLICY.getId())).thenReturn(ImmutableList.of(ALERTS_CONDITION_UPDATED));
        when(alertsNrqlConditionsApiMock.update(ALERTS_CONDITION_UPDATED.getId(), ALERTS_CONDITION_FROM_CONFIG)).thenReturn(ALERTS_CONDITION_UPDATED);

        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsNrqlConditionsApiMock);
        order.verify(alertsNrqlConditionsApiMock).list(POLICY.getId());
        order.verify(alertsNrqlConditionsApiMock).update(ALERTS_CONDITION_UPDATED.getId(), ALERTS_CONDITION_FROM_CONFIG);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldRemoveOldCondition() {
        // given
        when(alertsNrqlConditionsApiMock.list(POLICY.getId())).thenReturn(ImmutableList.of(ALERTS_CONDITION_DIFFERENT));
        when(alertsNrqlConditionsApiMock.create(POLICY.getId(), ALERTS_CONDITION_FROM_CONFIG)).thenReturn(ALERTS_CONDITION_SAME);

        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsNrqlConditionsApiMock);
        order.verify(alertsNrqlConditionsApiMock).list(POLICY.getId());
        order.verify(alertsNrqlConditionsApiMock).create(POLICY.getId(), ALERTS_CONDITION_FROM_CONFIG);
        order.verify(alertsNrqlConditionsApiMock).delete(ALERTS_CONDITION_DIFFERENT.getId());
        order.verifyNoMoreInteractions();
    }

    private static NrqlTermsConfiguration.NrqlTermsConfigurationBuilder createTermsConfiguration() {
        return NrqlTermsConfiguration.builder()
                .durationTerm(NrqlDurationTerm.DURATION_1)
                .operatorTerm(OperatorTerm.ABOVE)
                .priorityTerm(PriorityTerm.CRITICAL)
                .thresholdTerm(0.5f)
                .timeFunctionTerm(TimeFunctionTerm.ALL);
    }

    private static NrqlConfiguration createNrqlConfiguration() {
        return NrqlConfiguration.builder()
                .sinceValue(NrqlConfiguration.SinceValue.SINCE_1)
                .query("query")
                .build();
    }

    private static NrqlCondition createNrqlCondition(String conditionName) {
        return NrqlCondition.builder()
                .conditionName(conditionName)
                .enabled(ENABLED)
                .term(TERMS_CONFIGURATION)
                .valueFunction(VALUE_FUNCTION)
                .nrql(NRQL_CONFIGURATION)
                .build();
    }

    private static PolicyConfiguration createConfiguration() {
        return PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
                .nrqlCondition(NRQL_CONDITION)
                .build();
    }

    private static AlertsNrqlCondition.AlertsNrqlConditionBuilder createConditionBuilder() {
        return AlertsNrqlCondition.builder()
                .name(CONDITION_NAME)
                .enabled(ENABLED)
                .term(Terms.builder()
                        .duration(String.valueOf(TERMS_CONFIGURATION.getDurationTerm().getDuration()))
                        .operator(TERMS_CONFIGURATION.getOperatorTerm().name().toLowerCase())
                        .priority(TERMS_CONFIGURATION.getPriorityTerm().name().toLowerCase())
                        .threshold(String.valueOf(TERMS_CONFIGURATION.getThresholdTerm()))
                        .timeFunction(TERMS_CONFIGURATION.getTimeFunctionTerm().name().toLowerCase())
                        .build())
                .valueFunction(VALUE_FUNCTION.getValueString())
                .nrql(Nrql.builder()
                        .query(NRQL_CONFIGURATION.getQuery())
                        .sinceValue(String.valueOf(NRQL_CONFIGURATION.getSinceValue().getSince()))
                        .build());
    }
}
