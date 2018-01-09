package com.ocadotechnology.newrelic.alertsconfigurator;

import com.google.common.collect.ImmutableList;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ApmAppCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.Condition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql.NrqlCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql.NrqlConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.*;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.violationclosetimer.ViolationCloseTimer;
import com.ocadotechnology.newrelic.alertsconfigurator.exception.NewRelicSyncException;
import com.ocadotechnology.newrelic.alertsconfigurator.internal.entities.EntityResolver;
import com.ocadotechnology.newrelic.apiclient.model.conditions.AlertsNrqlCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.Nrql;
import com.ocadotechnology.newrelic.apiclient.model.conditions.Terms;
import com.ocadotechnology.newrelic.apiclient.model.policies.AlertsPolicy;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Optional;

import static com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql.SinceValue.SINCE_1_MINUTE;
import static java.lang.String.format;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

public class NrqlConditionConfiguratorTest extends AbstractConfiguratorTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private static final String POLICY_NAME = "policyName";
    private static final AlertsPolicy POLICY = AlertsPolicy.builder().id(42).name(POLICY_NAME).build();

    private static final ApmAppCondition.Metric APP_METRIC = ApmAppCondition.Metric.APDEX;
    private static final String CONDITION_NAME = "conditionName";
    private static final boolean ENABLED = true;
    private static final String SUM = "sum";
    public static final String NRQL = "SELECT COUNT(*) FROM Transaction";
    private static final String APPLICATION_NAME = "applicationName";
    private static final int APPLICATION_ENTITY_ID = 1;
    private static final ViolationCloseTimer VIOLATION_CLOSE_TIMER = ViolationCloseTimer.DURATION_1;
    private static final ApmAppCondition.ConditionScope CONDITION_SCOPE = ApmAppCondition.ConditionScope.INSTANCE;
    private static final TermsConfiguration TERMS_CONFIGURATION = createTermsConfiguration().build();

    private static final Condition APP_CONDITION = createAppCondition(CONDITION_NAME);
    private static final AlertsNrqlCondition ALERTS_CONDITION_SAME = createConditionBuilder().id(1).build();
    private static final AlertsNrqlCondition ALERTS_CONDITION_FROM_CONFIG = createConditionBuilder().build();
    private static final AlertsNrqlCondition ALERTS_CONDITION_UPDATED = createConditionBuilder().id(2).enabled(!ENABLED).build();
    private static final AlertsNrqlCondition ALERTS_CONDITION_DIFFERENT = createConditionBuilder().id(3).name("different").build();

    private static final PolicyConfiguration CONFIGURATION = createConfiguration();

    @Mock
    private EntityResolver entityResolverMock;
    @InjectMocks
    private NrqlConditionConfigurator testee;

    @Before
    public void setUp() {
        when(alertsPoliciesApiMock.getByName(POLICY_NAME)).thenReturn(Optional.of(POLICY));
        when(entityResolverMock.resolveEntities(apiMock, APP_CONDITION)).thenReturn(Collections.singletonList(APPLICATION_ENTITY_ID));
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
    public void shouldDoNothing_whenNoChannelsInConfiguration() {
        // given
        PolicyConfiguration config = PolicyConfiguration.builder()
            .policyName(POLICY_NAME)
            .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
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

    @Test
    public void shouldThrowException_whenConditionScopeInstanceButViolationCloseTimerIsNotSet() throws Exception {
        // given

        // then - exception
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("violationCloseTimer");

        // when
        ApmAppCondition.builder()
                .conditionName("condition name")
                .enabled(ENABLED)
                .application(APPLICATION_NAME)
                .metric(APP_METRIC)
                .conditionScope(ApmAppCondition.ConditionScope.INSTANCE)
                .term(TERMS_CONFIGURATION)
                .build();
    }

    private static PolicyConfiguration createConfiguration() {

        return PolicyConfiguration.builder()
            .policyName(POLICY_NAME)
            .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
            .nrqlCondition(createNrqlCondition())
            .build();
    }

    private static TermsConfiguration.TermsConfigurationBuilder createTermsConfiguration() {
        return TermsConfiguration.builder()
            .durationTerm(DurationTerm.DURATION_5)
            .operatorTerm(OperatorTerm.ABOVE)
            .priorityTerm(PriorityTerm.CRITICAL)
            .thresholdTerm(0.5f)
            .timeFunctionTerm(TimeFunctionTerm.ALL);
    }

    private static ApmAppCondition createAppCondition(String conditionName) {
        return ApmAppCondition.builder()
            .conditionName(conditionName)
            .enabled(ENABLED)
            .application(APPLICATION_NAME)
            .metric(APP_METRIC)
            .conditionScope(CONDITION_SCOPE)
            .violationCloseTimer(VIOLATION_CLOSE_TIMER)
            .term(TERMS_CONFIGURATION)
            .build();
    }

    private static AlertsNrqlCondition.AlertsNrqlConditionBuilder createConditionBuilder() {
        return AlertsNrqlCondition.builder()
            .name(CONDITION_NAME)
            .enabled(ENABLED)
                .valueFunction(SUM)
                .nrql(Nrql.builder()
                        .query(NRQL)
                        .since_value(SINCE_1_MINUTE.getSince())
                        .build())
            .term(Terms.builder()
                    .duration(String.valueOf(TERMS_CONFIGURATION.getDurationTerm().getDuration()))
                    .operator(TERMS_CONFIGURATION.getOperatorTerm().name().toLowerCase())
                    .priority(TERMS_CONFIGURATION.getPriorityTerm().name().toLowerCase())
                    .threshold(String.valueOf(TERMS_CONFIGURATION.getThresholdTerm()))
                    .timeFunction(TERMS_CONFIGURATION.getTimeFunctionTerm().name().toLowerCase())
                    .build());
    }

    private static NrqlCondition createNrqlCondition() {
        return NrqlCondition.builder()
                .conditionName(CONDITION_NAME)
                .enabled(true)
                .term(TERMS_CONFIGURATION)
                .valueFunction(NrqlCondition.ValueFunction.SUM)
                .nrql(NrqlConfiguration.builder()
                        .query(NRQL)
                        .sinceValue(SINCE_1_MINUTE)
                        .build())
                .build();
    }
}
