package com.ocadotechnology.newrelic.alertconfiguploader;

import com.google.common.collect.ImmutableList;
import com.ocado.newrelic.alertconfiguploader.ConditionSynchronizer;
import com.ocadotechnology.newrelic.alertconfiguploader.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertconfiguploader.configuration.condition.ApmAppCondition;
import com.ocadotechnology.newrelic.alertconfiguploader.configuration.condition.Condition;
import com.ocadotechnology.newrelic.alertconfiguploader.configuration.condition.ConditionType;
import com.ocadotechnology.newrelic.alertconfiguploader.configuration.condition.terms.*;
import com.ocadotechnology.newrelic.alertconfiguploader.exception.NewRelicSyncException;
import com.ocadotechnology.newrelic.alertconfiguploader.internal.entities.EntityResolver;
import com.ocadotechnology.newrelic.api.model.conditions.AlertsCondition;
import com.ocadotechnology.newrelic.api.model.conditions.Terms;
import com.ocadotechnology.newrelic.api.model.policies.AlertsPolicy;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Optional;

import static java.lang.String.format;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

public class ConditionSynchronizerTest extends AbstractSynchronizerTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private static final String POLICY_NAME = "policyName";
    private static final AlertsPolicy POLICY = AlertsPolicy.builder().id(42).name(POLICY_NAME).build();

    private static final ApmAppCondition.Metric APP_METRIC = ApmAppCondition.Metric.APDEX;
    private static final String CONDITION_NAME = "conditionName";
    private static final boolean ENABLED = true;
    private static final String ENTITY_NAME = "entityName";
    private static final int ENTITY_ID = 1;
    private static final ApmAppCondition.ConditionScope CONDITION_SCOPE = ApmAppCondition.ConditionScope.APPLICATION;
    private static final TermsConfiguration TERMS_CONFIGURATION = createTermsConfiguration().build();

    private static final Condition APP_CONDITION = createAppCondition(CONDITION_NAME);
    private static final AlertsCondition ALERTS_CONDITION_SAME = createConditionBuilder().id(1).build();
    private static final AlertsCondition ALERTS_CONDITION_FROM_CONFIG = createConditionBuilder().build();
    private static final AlertsCondition ALERTS_CONDITION_UPDATED = createConditionBuilder().id(2).enabled(!ENABLED).build();
    private static final AlertsCondition ALERTS_CONDITION_DIFFERENT = createConditionBuilder().id(3).name("different").build();

    private static final PolicyConfiguration CONFIGURATION = createConfiguration();

    @Mock
    private EntityResolver entityResolverMock;
    @InjectMocks
    private ConditionSynchronizer testee;

    @Before
    public void setUp() {
        when(alertsPoliciesApiMock.getByName(POLICY_NAME)).thenReturn(Optional.of(POLICY));
        when(entityResolverMock.resolveEntities(apiMock, APP_CONDITION)).thenReturn(Collections.singletonList(ENTITY_ID));
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
            .build();

        // when
        testee.sync(config);

        // then
        InOrder order = inOrder(alertsConditionsApiMock);
        order.verify(alertsConditionsApiMock).list(POLICY.getId());
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldCreateCondition() {
        // given
        when(alertsConditionsApiMock.list(POLICY.getId())).thenReturn(ImmutableList.of());
        when(alertsConditionsApiMock.create(POLICY.getId(), ALERTS_CONDITION_FROM_CONFIG)).thenReturn(ALERTS_CONDITION_SAME);

        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsConditionsApiMock);
        order.verify(alertsConditionsApiMock).list(POLICY.getId());
        order.verify(alertsConditionsApiMock).create(POLICY.getId(), ALERTS_CONDITION_FROM_CONFIG);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldUpdateCondition() {
        // given
        when(alertsConditionsApiMock.list(POLICY.getId())).thenReturn(ImmutableList.of(ALERTS_CONDITION_UPDATED));
        when(alertsConditionsApiMock.update(ALERTS_CONDITION_UPDATED.getId(), ALERTS_CONDITION_FROM_CONFIG)).thenReturn(ALERTS_CONDITION_UPDATED);

        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsConditionsApiMock);
        order.verify(alertsConditionsApiMock).list(POLICY.getId());
        order.verify(alertsConditionsApiMock).update(ALERTS_CONDITION_UPDATED.getId(), ALERTS_CONDITION_FROM_CONFIG);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldRemoveOldCondition() {
        // given
        when(alertsConditionsApiMock.list(POLICY.getId())).thenReturn(ImmutableList.of(ALERTS_CONDITION_DIFFERENT));
        when(alertsConditionsApiMock.create(POLICY.getId(), ALERTS_CONDITION_FROM_CONFIG)).thenReturn(ALERTS_CONDITION_SAME);

        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsConditionsApiMock);
        order.verify(alertsConditionsApiMock).list(POLICY.getId());
        order.verify(alertsConditionsApiMock).create(POLICY.getId(), ALERTS_CONDITION_FROM_CONFIG);
        order.verify(alertsConditionsApiMock).delete(ALERTS_CONDITION_DIFFERENT.getId());
        order.verifyNoMoreInteractions();
    }

    private static PolicyConfiguration createConfiguration() {
        return PolicyConfiguration.builder()
            .policyName(POLICY_NAME)
            .condition(APP_CONDITION)
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
            .entity(ENTITY_NAME)
            .metric(APP_METRIC)
            .conditionScope(CONDITION_SCOPE)
            .term(TERMS_CONFIGURATION)
            .build();
    }

    private static AlertsCondition.AlertsConditionBuilder createConditionBuilder() {
        return AlertsCondition.builder()
            .type(ConditionType.APM_APP.getTypeString())
            .name(CONDITION_NAME)
            .enabled(ENABLED)
            .entity(ENTITY_ID)
            .metric(APP_METRIC.name().toLowerCase())
            .conditionScope(CONDITION_SCOPE.name().toLowerCase())
            .term(Terms.builder()
                    .duration(String.valueOf(TERMS_CONFIGURATION.getDurationTerm().getDuration()))
                    .operator(TERMS_CONFIGURATION.getOperatorTerm().name().toLowerCase())
                    .priority(TERMS_CONFIGURATION.getPriorityTerm().name().toLowerCase())
                    .threshold(String.valueOf(TERMS_CONFIGURATION.getThresholdTerm()))
                    .timeFunction(TERMS_CONFIGURATION.getTimeFunctionTerm().name().toLowerCase())
                    .build());
    }
}
