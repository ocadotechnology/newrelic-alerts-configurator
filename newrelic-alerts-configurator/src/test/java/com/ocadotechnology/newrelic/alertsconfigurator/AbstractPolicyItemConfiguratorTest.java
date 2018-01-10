package com.ocadotechnology.newrelic.alertsconfigurator;

import com.google.common.collect.ImmutableList;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ApmAppCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.*;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.violationclosetimer.ViolationCloseTimer;
import com.ocadotechnology.newrelic.alertsconfigurator.exception.NewRelicSyncException;
import com.ocadotechnology.newrelic.alertsconfigurator.internal.entities.EntityResolver;
import com.ocadotechnology.newrelic.apiclient.PolicyItemApi;
import com.ocadotechnology.newrelic.apiclient.model.PolicyItem;
import com.ocadotechnology.newrelic.apiclient.model.policies.AlertsPolicy;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.util.Optional;

import static java.lang.String.format;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

public abstract class AbstractPolicyItemConfiguratorTest<T extends PolicyItemConfigurator, U extends PolicyItem> extends AbstractConfiguratorTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    protected static final ApmAppCondition.Metric APP_METRIC = ApmAppCondition.Metric.APDEX;
    protected static final String POLICY_NAME = "policyName";
    protected static final PolicyConfiguration.IncidentPreference INCIDENT_PREFERENCE = PolicyConfiguration.IncidentPreference.PER_POLICY;
    protected static final AlertsPolicy POLICY = AlertsPolicy.builder()
            .id(42)
            .name(POLICY_NAME)
            .incidentPreference(INCIDENT_PREFERENCE.name())
            .build();


    protected static final String CONDITION_NAME = "conditionName";
    protected static final ApmAppCondition.ConditionScope CONDITION_SCOPE = ApmAppCondition.ConditionScope.INSTANCE;
    protected static final boolean ENABLED = true;
    protected static final String APPLICATION_NAME = "applicationName";
    protected static final int APPLICATION_ENTITY_ID = 1;
    protected static final ViolationCloseTimer VIOLATION_CLOSE_TIMER = ViolationCloseTimer.DURATION_1;
    protected static final TermsConfiguration TERMS_CONFIGURATION = createTermsConfiguration().build();

    @Mock
    EntityResolver entityResolverMock;
    protected T testee;

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
        testee.sync(getPolicyConfiguration());
    }

    @Test
    public void shouldDoNothing_whenNoChannelsInConfiguration() {
        PolicyItemApi<U> itemApiMock = getPolicyItemApiMock();

        // given
        PolicyConfiguration config = PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
                .build();

        // when
        testee.sync(config);

        // then
        InOrder order = inOrder(itemApiMock);
        order.verify(itemApiMock).list(POLICY.getId());
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


    @Test
    public void shouldCreateCondition() {
        U itemFromConfig = getItemFromConfig();
        U itemSame = getItemSame();
        PolicyConfiguration policyConfiguration = getPolicyConfiguration();
        PolicyItemApi<U> itemApiMock = getPolicyItemApiMock();

        // given
        when(itemApiMock.list(POLICY.getId())).thenReturn(ImmutableList.of());
        when(itemApiMock.create(POLICY.getId(), itemFromConfig)).thenReturn(itemSame);

        // when
        testee.sync(policyConfiguration);

        // then
        InOrder order = inOrder(itemApiMock);
        order.verify(itemApiMock).list(POLICY.getId());
        order.verify(itemApiMock).create(POLICY.getId(), itemFromConfig);
        order.verifyNoMoreInteractions();
    }


    @Test
    public void shouldUpdateCondition() {
        U itemFromConfig = getItemFromConfig();
        U itemUpdated = getItemUpdated();
        PolicyConfiguration policyConfiguration = getPolicyConfiguration();
        PolicyItemApi<U> itemApiMock = getPolicyItemApiMock();

        // given
        when(itemApiMock.list(POLICY.getId())).thenReturn(ImmutableList.of(itemUpdated));
        when(itemApiMock.update(itemUpdated.getId(), itemFromConfig)).thenReturn(itemUpdated);

        // when
        testee.sync(policyConfiguration);

        // then
        InOrder order = inOrder(itemApiMock);
        order.verify(itemApiMock).list(POLICY.getId());
        order.verify(itemApiMock).update(itemUpdated.getId(), itemFromConfig);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldRemoveOldCondition() {
        U itemFromConfig = getItemFromConfig();
        U itemSame = getItemSame();
        U itemDifferent = getItemDifferent();
        PolicyConfiguration policyConfiguration = getPolicyConfiguration();
        PolicyItemApi<U> itemApiMock = getPolicyItemApiMock();

        // given
        when(itemApiMock.list(POLICY.getId())).thenReturn(ImmutableList.of(itemDifferent));
        when(itemApiMock.create(POLICY.getId(), itemFromConfig)).thenReturn(itemSame);

        // when
        testee.sync(policyConfiguration);

        // then
        InOrder order = inOrder(itemApiMock);
        order.verify(itemApiMock).list(POLICY.getId());
        order.verify(itemApiMock).create(POLICY.getId(), itemFromConfig);
        order.verify(itemApiMock).delete(itemDifferent.getId());
        order.verifyNoMoreInteractions();
    }

    protected abstract PolicyConfiguration getPolicyConfiguration();

    protected abstract PolicyItemApi<U> getPolicyItemApiMock();

    protected abstract U getItemFromConfig();

    protected abstract U getItemSame();

    protected abstract U getItemUpdated();

    protected abstract U getItemDifferent();


    private static TermsConfiguration.TermsConfigurationBuilder createTermsConfiguration() {
        return TermsConfiguration.builder()
                .durationTerm(DurationTerm.DURATION_5)
                .operatorTerm(OperatorTerm.ABOVE)
                .priorityTerm(PriorityTerm.CRITICAL)
                .thresholdTerm(0.5f)
                .timeFunctionTerm(TimeFunctionTerm.ALL);
    }

    static ApmAppCondition createAppCondition(String conditionName) {
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
}
