package com.ocado.pandateam.newrelic.sync;

import com.google.common.collect.ImmutableList;
import com.ocado.pandateam.newrelic.api.model.applications.Application;
import com.ocado.pandateam.newrelic.api.model.conditions.Terms;
import com.ocado.pandateam.newrelic.api.model.conditions.external.AlertsExternalServiceCondition;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.sync.configuration.ExternalServiceConditionConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.condition.ApmAppCondition;
import com.ocado.pandateam.newrelic.sync.configuration.condition.ApmExternalServiceCondition;
import com.ocado.pandateam.newrelic.sync.configuration.condition.ExternalServiceCondition;
import com.ocado.pandateam.newrelic.sync.configuration.condition.ExternalServiceConditionType;
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

import java.util.Collections;
import java.util.Optional;

import static java.lang.String.format;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class ExternalServiceConditionSynchronizerTest extends AbstractSynchronizerTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private ExternalServiceConditionSynchronizer testee;
    private ExternalServiceConditionConfiguration configuration = createConfiguration();

    private static final String POLICY_NAME = "policyName";
    private static final AlertsPolicy POLICY = AlertsPolicy.builder().id(42).name(POLICY_NAME).build();

    private static final ApmAppCondition.Metric METRIC = ApmAppCondition.Metric.APDEX;
    private static final String CONDITION_NAME = "conditionName";
    private static final boolean ENABLED = true;
    private static final String APPLICATION_NAME = "applicationName";
    private static final String EXTERNAL_SERVICE_URL = "externalServiceUrl";
    private static final DurationTerm DURATION_TERM = DurationTerm.DURATION_5;
    private static final OperatorTerm OPERATOR_TERM = OperatorTerm.ABOVE;
    private static final PriorityTerm PRIORITY_TERM = PriorityTerm.CRITICAL;
    private static final Float THRESHOLD_TERM = 0.5f;
    private static final TimeFunctionTerm TIME_FUNCTION_TERM = TimeFunctionTerm.ALL;
    private static final TermsConfiguration TERMS_CONFIGURATION = createTermsConfiguration().build();

    private static final ExternalServiceCondition EXTERNAL_SERVICE_CONDITION = createCondition(CONDITION_NAME);
    private static final Application APPLICATION = Application.builder().id(1).name(APPLICATION_NAME).build();
    private static final AlertsExternalServiceCondition ALERTS_EXTERNAL_SERVICE_CONDITION_SAME = createDefaultAlertsConditionBuilder().id(1).build();
    private static final AlertsExternalServiceCondition ALERTS_EXTERNAL_SERVICE_CONDITION_MAPPED = createDefaultAlertsConditionBuilder().build();
    private static final AlertsExternalServiceCondition ALERTS_EXTERNAL_SERVICE_CONDITION_UPDATED = createDefaultAlertsConditionBuilder().id(2).enabled(!ENABLED).build();
    private static final AlertsExternalServiceCondition ALERTS_EXTERNAL_SERVICE_CONDITION_DIFFERENT = createDefaultAlertsConditionBuilder().id(3).name("different").build();

    @Before
    public void setUp() {
        testee = new ExternalServiceConditionSynchronizer(apiMock, configuration);
        when(alertsPoliciesApiMock.getByName(eq(POLICY_NAME))).thenReturn(Optional.of(POLICY));
        when(applicationsApiMock.getByName(APPLICATION_NAME)).thenReturn(Optional.of(APPLICATION));
    }

    @Test
    public void shouldThrowException_whenPolicyDoesNotExist() {
        // given
        when(alertsPoliciesApiMock.getByName(eq(POLICY_NAME))).thenReturn(Optional.empty());

        // then - exception
        expectedException.expect(NewRelicSyncException.class);
        expectedException.expectMessage(format("Policy %s does not exist", POLICY_NAME));

        // when
        testee.sync();
    }

    @Test
    public void shouldCreateCondition() {
        // given
        when(alertsExternalServiceConditionsApiMock.list(eq(POLICY.getId()))).thenReturn(ImmutableList.of());
        when(alertsExternalServiceConditionsApiMock.create(eq(POLICY.getId()), eq(ALERTS_EXTERNAL_SERVICE_CONDITION_MAPPED)))
            .thenReturn(ALERTS_EXTERNAL_SERVICE_CONDITION_SAME);

        // when
        testee.sync();

        // then
        verify(alertsExternalServiceConditionsApiMock).list(eq(POLICY.getId()));
        verify(alertsExternalServiceConditionsApiMock).create(eq(POLICY.getId()), eq(ALERTS_EXTERNAL_SERVICE_CONDITION_MAPPED));
        verifyNoMoreInteractions(alertsExternalServiceConditionsApiMock);
    }

    @Test
    public void shouldUpdateCondition() {
        // given
        when(alertsExternalServiceConditionsApiMock.list(eq(POLICY.getId()))).thenReturn(ImmutableList.of(ALERTS_EXTERNAL_SERVICE_CONDITION_UPDATED));
        when(alertsExternalServiceConditionsApiMock.update(eq(ALERTS_EXTERNAL_SERVICE_CONDITION_UPDATED.getId()), eq(ALERTS_EXTERNAL_SERVICE_CONDITION_MAPPED)))
            .thenReturn(ALERTS_EXTERNAL_SERVICE_CONDITION_UPDATED);

        // when
        testee.sync();

        // then
        verify(alertsExternalServiceConditionsApiMock).list(eq(POLICY.getId()));
        verify(alertsExternalServiceConditionsApiMock).update(eq(ALERTS_EXTERNAL_SERVICE_CONDITION_UPDATED.getId()), eq(ALERTS_EXTERNAL_SERVICE_CONDITION_MAPPED));
        verifyNoMoreInteractions(alertsExternalServiceConditionsApiMock);
    }

    @Test
    public void shouldRemoveOldCondition() {
        // given
        when(alertsExternalServiceConditionsApiMock.list(eq(POLICY.getId())))
            .thenReturn(ImmutableList.of(ALERTS_EXTERNAL_SERVICE_CONDITION_DIFFERENT));
        when(alertsExternalServiceConditionsApiMock.create(eq(POLICY.getId()), eq(ALERTS_EXTERNAL_SERVICE_CONDITION_MAPPED)))
            .thenReturn(ALERTS_EXTERNAL_SERVICE_CONDITION_SAME);

        // when
        testee.sync();

        // then
        verify(alertsExternalServiceConditionsApiMock).list(eq(POLICY.getId()));
        verify(alertsExternalServiceConditionsApiMock).create(eq(POLICY.getId()), eq(ALERTS_EXTERNAL_SERVICE_CONDITION_MAPPED));
        verify(alertsExternalServiceConditionsApiMock).delete(eq(ALERTS_EXTERNAL_SERVICE_CONDITION_DIFFERENT.getId()));
        verifyNoMoreInteractions(alertsExternalServiceConditionsApiMock);
    }

    private static ExternalServiceConditionConfiguration createConfiguration() {
        return ExternalServiceConditionConfiguration.builder()
            .policyName(POLICY_NAME)
            .externalServiceConditions(
                Collections.singletonList(
                    EXTERNAL_SERVICE_CONDITION
                )
            )
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

    private static ApmExternalServiceCondition createCondition(String conditionName) {
        return ApmExternalServiceCondition.builder()
            .conditionName(conditionName)
            .enabled(ENABLED)
            .entities(Collections.singletonList(APPLICATION_NAME))
            .metric(METRIC)
            .externalServiceUrl(EXTERNAL_SERVICE_URL)
            .terms(
                Collections.singletonList(
                    TERMS_CONFIGURATION
                )
            )
            .build();
    }

    private static AlertsExternalServiceCondition.AlertsExternalServiceConditionBuilder createDefaultAlertsConditionBuilder() {
        return AlertsExternalServiceCondition.builder()
            .type(ExternalServiceConditionType.APM.getTypeString())
            .name(CONDITION_NAME)
            .enabled(ENABLED)
            .entities(ImmutableList.of(APPLICATION.getId()))
            .metric(METRIC.name().toLowerCase())
            .externalServiceUrl(EXTERNAL_SERVICE_URL)
            .terms(ImmutableList.of(
                Terms.builder()
                    .duration(TERMS_CONFIGURATION.getDurationTerm())
                    .operator(TERMS_CONFIGURATION.getOperatorTerm())
                    .priority(TERMS_CONFIGURATION.getPriorityTerm())
                    .threshold(TERMS_CONFIGURATION.getThresholdTerm())
                    .timeFunction(TERMS_CONFIGURATION.getTimeFunctionTerm())
                    .build()
                )
            );
    }
}
