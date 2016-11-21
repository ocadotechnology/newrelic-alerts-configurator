package com.ocado.pandateam.newrelic.sync;

import com.google.common.collect.ImmutableList;
import com.ocado.pandateam.newrelic.api.model.applications.Application;
import com.ocado.pandateam.newrelic.api.model.conditions.AlertsCondition;
import com.ocado.pandateam.newrelic.api.model.conditions.Terms;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.api.model.transactions.KeyTransaction;
import com.ocado.pandateam.newrelic.sync.configuration.PolicyConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.condition.ApmAppCondition;
import com.ocado.pandateam.newrelic.sync.configuration.condition.ApmKeyTransactionCondition;
import com.ocado.pandateam.newrelic.sync.configuration.condition.Condition;
import com.ocado.pandateam.newrelic.sync.configuration.condition.ConditionType;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.DurationTerm;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.OperatorTerm;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.PriorityTerm;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.TermsConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.TimeFunctionTerm;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

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
    private static final ApmKeyTransactionCondition.Metric KT_METRIC = ApmKeyTransactionCondition.Metric.ERROR_COUNT;
    private static final String APP_CONDITION_NAME = "appConditionName";
    private static final String KEY_TRANSACTION_CONDITION_NAME = "ktConditionName";
    private static final boolean ENABLED = true;

    private static final ApmAppCondition.ConditionScope CONDITION_SCOPE = ApmAppCondition.ConditionScope.APPLICATION;
    private static final DurationTerm DURATION_TERM = DurationTerm.DURATION_5;
    private static final OperatorTerm OPERATOR_TERM = OperatorTerm.ABOVE;
    private static final PriorityTerm PRIORITY_TERM = PriorityTerm.CRITICAL;
    private static final Float THRESHOLD_TERM = 0.5f;
    private static final TimeFunctionTerm TIME_FUNCTION_TERM = TimeFunctionTerm.ALL;
    private static final TermsConfiguration TERMS_CONFIGURATION = createTermsConfiguration().build();

    private static final Condition APP_CONDITION = createAppCondition(APP_CONDITION_NAME);
    private static final Condition KEY_TRANSACTION_CONDITION = createKtCondition(KEY_TRANSACTION_CONDITION_NAME);

    private static final String KEY_TRANSACTION_NAME = "keyTransaction";
    private static final KeyTransaction KEY_TRANSACTION = KeyTransaction.builder().id(666).name(KEY_TRANSACTION_NAME).build();
    private static final String APPLICATION_NAME = "applicationName";
    private static final Application APPLICATION = Application.builder().id(1).name(APPLICATION_NAME).build();

    private static final AlertsCondition ALERTS_CONDITION_SAME = createDefaultAlertsConditionBuilder().id(1).build();
    private static final AlertsCondition ALERTS_CONDITION_FROM_CONFIG = createDefaultAlertsConditionBuilder().build();
    private static final AlertsCondition ALERTS_CONDITION_UPDATED = createDefaultAlertsConditionBuilder().id(2).enabled(!ENABLED).build();
    private static final AlertsCondition ALERTS_CONDITION_DIFFERENT = createDefaultAlertsConditionBuilder().id(3).name("different").build();
    private static final AlertsCondition ALERTS_CONDITION_KEY_TRANSACTION_SAME = createAlertsKtConditionBuilder().id(15).build();
    private static final AlertsCondition ALERTS_CONDITION_KEY_TRANSACTION_FROM_CONFIG = createAlertsKtConditionBuilder().build();

    private ConditionSynchronizer testee;
    private static final PolicyConfiguration CONFIGURATION = createConfiguration();

    @Before
    public void setUp() {
        testee = new ConditionSynchronizer(apiMock);
        when(alertsPoliciesApiMock.getByName(POLICY_NAME)).thenReturn(Optional.of(POLICY));
        when(applicationsApiMock.getByName(APPLICATION_NAME)).thenReturn(Optional.of(APPLICATION));
        when(keyTransactionsApiMock.getByName(KEY_TRANSACTION_NAME)).thenReturn(Optional.of(KEY_TRANSACTION));
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
        when(alertsConditionsApiMock.create(POLICY.getId(), ALERTS_CONDITION_KEY_TRANSACTION_FROM_CONFIG)).thenReturn(ALERTS_CONDITION_KEY_TRANSACTION_SAME);

        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsConditionsApiMock);
        order.verify(alertsConditionsApiMock).list(POLICY.getId());
        order.verify(alertsConditionsApiMock).create(POLICY.getId(), ALERTS_CONDITION_FROM_CONFIG);
        order.verify(alertsConditionsApiMock).create(POLICY.getId(), ALERTS_CONDITION_KEY_TRANSACTION_FROM_CONFIG);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldUpdateCondition() {
        // given
        when(alertsConditionsApiMock.list(POLICY.getId())).thenReturn(ImmutableList.of(ALERTS_CONDITION_UPDATED));
        when(alertsConditionsApiMock.update(ALERTS_CONDITION_UPDATED.getId(), ALERTS_CONDITION_FROM_CONFIG)).thenReturn(ALERTS_CONDITION_UPDATED);
        when(alertsConditionsApiMock.create(POLICY.getId(), ALERTS_CONDITION_KEY_TRANSACTION_FROM_CONFIG)).thenReturn(ALERTS_CONDITION_KEY_TRANSACTION_SAME);

        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsConditionsApiMock);
        order.verify(alertsConditionsApiMock).list(POLICY.getId());
        order.verify(alertsConditionsApiMock).update(ALERTS_CONDITION_UPDATED.getId(), ALERTS_CONDITION_FROM_CONFIG);
        order.verify(alertsConditionsApiMock).create(POLICY.getId(), ALERTS_CONDITION_KEY_TRANSACTION_FROM_CONFIG);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldRemoveOldCondition() {
        // given
        when(alertsConditionsApiMock.list(POLICY.getId())).thenReturn(ImmutableList.of(ALERTS_CONDITION_DIFFERENT));
        when(alertsConditionsApiMock.create(POLICY.getId(), ALERTS_CONDITION_FROM_CONFIG)).thenReturn(ALERTS_CONDITION_SAME);
        when(alertsConditionsApiMock.create(POLICY.getId(), ALERTS_CONDITION_KEY_TRANSACTION_FROM_CONFIG)).thenReturn(ALERTS_CONDITION_KEY_TRANSACTION_SAME);

        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsConditionsApiMock);
        order.verify(alertsConditionsApiMock).list(POLICY.getId());
        order.verify(alertsConditionsApiMock).create(POLICY.getId(), ALERTS_CONDITION_FROM_CONFIG);
        order.verify(alertsConditionsApiMock).create(POLICY.getId(), ALERTS_CONDITION_KEY_TRANSACTION_FROM_CONFIG);
        order.verify(alertsConditionsApiMock).delete(ALERTS_CONDITION_DIFFERENT.getId());
        order.verifyNoMoreInteractions();
    }

    private static PolicyConfiguration createConfiguration() {
        return PolicyConfiguration.builder()
            .policyName(POLICY_NAME)
            .condition(APP_CONDITION)
            .condition(KEY_TRANSACTION_CONDITION)
            .build();
    }

    private static TermsConfiguration.TermsConfigurationBuilder createTermsConfiguration() {
        return TermsConfiguration.builder()
            .durationTerm(DURATION_TERM)
            .operatorTerm(OPERATOR_TERM)
            .priorityTerm(PRIORITY_TERM)
            .thresholdTerm(THRESHOLD_TERM)
            .timeFunctionTerm(TIME_FUNCTION_TERM);
    }

    private static ApmAppCondition createAppCondition(String conditionName) {
        return ApmAppCondition.builder()
            .conditionName(conditionName)
            .enabled(ENABLED)
            .entity(APPLICATION_NAME)
            .metric(APP_METRIC)
            .conditionScope(CONDITION_SCOPE)
            .term(TERMS_CONFIGURATION)
            .build();
    }

    private static ApmKeyTransactionCondition createKtCondition(String conditionName) {
        return ApmKeyTransactionCondition.builder()
            .conditionName(conditionName)
            .enabled(ENABLED)
            .entity(KEY_TRANSACTION_NAME)
            .metric(KT_METRIC)
            .term(TERMS_CONFIGURATION)
            .build();
    }

    private static AlertsCondition.AlertsConditionBuilder createDefaultAlertsConditionBuilder() {
        return AlertsCondition.builder()
            .type(ConditionType.APM_APP.getTypeString())
            .name(APP_CONDITION_NAME)
            .enabled(ENABLED)
            .entity(APPLICATION.getId())
            .metric(APP_METRIC.name().toLowerCase())
            .conditionScope(CONDITION_SCOPE.name().toLowerCase())
            .term(Terms.builder()
                .duration(String.valueOf(TERMS_CONFIGURATION.getDurationTerm().getDuration()))
                .operator(TERMS_CONFIGURATION.getOperatorTerm().name().toLowerCase())
                .priority(TERMS_CONFIGURATION.getPriorityTerm().name().toLowerCase())
                .threshold(String.valueOf(TERMS_CONFIGURATION.getThresholdTerm()))
                .timeFunction(TERMS_CONFIGURATION.getTimeFunctionTerm().name().toLowerCase())
                .build()
            );
    }

    private static AlertsCondition.AlertsConditionBuilder createAlertsKtConditionBuilder() {
        return AlertsCondition.builder()
            .type(ConditionType.APM_KEY_TRANSACTION.getTypeString())
            .name(KEY_TRANSACTION_CONDITION_NAME)
            .enabled(ENABLED)
            .entity(KEY_TRANSACTION.getId())
            .metric(KT_METRIC.name().toLowerCase())
            .term(Terms.builder()
                .duration(String.valueOf(TERMS_CONFIGURATION.getDurationTerm().getDuration()))
                .operator(TERMS_CONFIGURATION.getOperatorTerm().name().toLowerCase())
                .priority(TERMS_CONFIGURATION.getPriorityTerm().name().toLowerCase())
                .threshold(String.valueOf(TERMS_CONFIGURATION.getThresholdTerm()))
                .timeFunction(TERMS_CONFIGURATION.getTimeFunctionTerm().name().toLowerCase())
                .build()
            );
    }
}
