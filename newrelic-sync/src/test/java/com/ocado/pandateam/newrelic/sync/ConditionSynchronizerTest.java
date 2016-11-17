package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.sync.configuration.ConditionConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.condition.ApmAppCondition;
import com.ocado.pandateam.newrelic.sync.configuration.condition.ConditionScope;
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
import static org.mockito.Mockito.when;

public class ConditionSynchronizerTest extends AbstractSynchronizerTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private ConditionSynchronizer testee;
    private ConditionConfiguration configuration = createConfiguration();

    private static final String POLICY_NAME = "policyName";
    private static final AlertsPolicy POLICY = AlertsPolicy.builder().id(42).name(POLICY_NAME).build();

    private static final ApmAppCondition.Metric METRIC = ApmAppCondition.Metric.APDEX;
    private static final String CONDITION_NAME = "conditionName";
    private static final boolean ENABLED = true;
    private static final String APPLICATION_NAME = "applicationName";
    private static final ConditionScope CONDITION_SCOPE = ConditionScope.APPLICATION;
    private static final DurationTerm DURATION_TERM = DurationTerm.DURATION_5;
    private static final OperatorTerm OPERATOR_TERM = OperatorTerm.ABOVE;
    private static final PriorityTerm PRIORITY_TERM = PriorityTerm.CRITICAL;
    private static final Integer THRESHOLD_TERM = 1;
    private static final TimeFunctionTerm TIME_FUNCTION_TERM = TimeFunctionTerm.ALL;

    @Before
    public void setUp() {
        testee = new ConditionSynchronizer(apiMock, configuration);
        when(alertsPoliciesApiMock.getByName(eq(POLICY_NAME))).thenReturn(Optional.of(POLICY));
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

        // when

        // then
    }

    @Test
    public void shouldUpdateCondition() {
        // given

        // when

        // then
    }

    @Test
    public void shouldRemoveOldCondition() {
        // given

        // when

        // then
    }

    private ConditionConfiguration createConfiguration() {
        return ConditionConfiguration.builder()
            .policyName(POLICY_NAME)
            .conditions(
                Collections.singletonList(
                    ApmAppCondition.builder()
                        .conditionName(CONDITION_NAME)
                        .enabled(ENABLED)
                        .entities(Collections.singletonList(APPLICATION_NAME))
                        .metric(METRIC)
                        .conditionScope(CONDITION_SCOPE)
                        .terms(
                            Collections.singletonList(
                                TermsConfiguration.builder()
                                    .durationTerm(DURATION_TERM)
                                    .operatorTerm(OPERATOR_TERM)
                                    .priorityTerm(PRIORITY_TERM)
                                    .thresholdTerm(THRESHOLD_TERM)
                                    .timeFunctionTerm(TIME_FUNCTION_TERM)
                                    .build()
                            )
                        )
                        .build()
                )
            )
            .build();
    }
}
