package com.ocadotechnology.newrelic.alertsconfigurator;

import com.google.common.collect.ImmutableList;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration.IncidentPreference;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ApmExternalServiceCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ExternalServiceCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ExternalServiceConditionType;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.DurationTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.OperatorTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.PriorityTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TimeFunctionTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.exception.NewRelicSyncException;
import com.ocadotechnology.newrelic.alertsconfigurator.internal.entities.EntityResolver;
import com.ocadotechnology.newrelic.apiclient.model.conditions.Terms;
import com.ocadotechnology.newrelic.apiclient.model.conditions.external.AlertsExternalServiceCondition;
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

import static java.lang.String.format;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

public class ExternalServiceConditionConfiguratorTest extends AbstractConfiguratorTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private static final String POLICY_NAME = "policyName";
    private static final IncidentPreference INCIDENT_PREFERENCE = IncidentPreference.PER_POLICY;
    private static final AlertsPolicy POLICY = AlertsPolicy.builder()
            .id(42)
            .name(POLICY_NAME)
            .incidentPreference(INCIDENT_PREFERENCE.name())
            .build();

    private static final ApmExternalServiceCondition.Metric METRIC = ApmExternalServiceCondition.Metric.RESPONSE_TIME_AVERAGE;
    private static final String CONDITION_NAME = "conditionName";
    private static final boolean ENABLED = true;
    private static final String APPLICATION_NAME = "entityName";
    private static final int APPLICATION_ENTITY_ID = 1;
    private static final String EXTERNAL_SERVICE_URL = "externalServiceUrl";
    private static final TermsConfiguration TERMS_CONFIGURATION = createTermsConfiguration().build();

    private static final ExternalServiceCondition EXTERNAL_SERVICE_CONDITION = createCondition(CONDITION_NAME);
    private static final AlertsExternalServiceCondition ALERTS_CONDITION_SAME = createConditionBuilder().id(1).build();
    private static final AlertsExternalServiceCondition ALERTS_CONDITION_FROM_CONFIG = createConditionBuilder().build();
    private static final AlertsExternalServiceCondition ALERTS_CONDITION_UPDATED = createConditionBuilder().id(2).enabled(!ENABLED).build();
    private static final AlertsExternalServiceCondition ALERTS_CONDITION_DIFFERENT = createConditionBuilder().id(3).name("different").build();

    private static final PolicyConfiguration CONFIGURATION = createConfiguration();

    @Mock
    private EntityResolver entityResolverMock;
    @InjectMocks
    private ExternalServiceConditionConfigurator testee;

    @Before
    public void setUp() {
        when(alertsPoliciesApiMock.getByName(POLICY_NAME)).thenReturn(Optional.of(POLICY));
        when(entityResolverMock.resolveEntities(apiMock, EXTERNAL_SERVICE_CONDITION)).thenReturn(Collections.singletonList(APPLICATION_ENTITY_ID));
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
            .incidentPreference(INCIDENT_PREFERENCE)
            .build();

        // when
        testee.sync(config);

        // then
        InOrder order = inOrder(alertsExternalServiceConditionsApiMock);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldDoNothing_whenEmptyConditionsInConfiguration() {
        // given
        PolicyConfiguration config = PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .incidentPreference(INCIDENT_PREFERENCE)
                .externalServiceConditions(Collections.emptyList())
                .build();

        // when
        testee.sync(config);

        // then
        InOrder order = inOrder(alertsExternalServiceConditionsApiMock);
        order.verify(alertsExternalServiceConditionsApiMock).list(POLICY.getId());
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldCreateCondition() {
        // given
        when(alertsExternalServiceConditionsApiMock.list(POLICY.getId())).thenReturn(ImmutableList.of());
        when(alertsExternalServiceConditionsApiMock.create(POLICY.getId(), ALERTS_CONDITION_FROM_CONFIG))
            .thenReturn(ALERTS_CONDITION_SAME);

        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsExternalServiceConditionsApiMock);
        order.verify(alertsExternalServiceConditionsApiMock).list(POLICY.getId());
        order.verify(alertsExternalServiceConditionsApiMock).create(POLICY.getId(), ALERTS_CONDITION_FROM_CONFIG);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldUpdateCondition() {
        // given
        when(alertsExternalServiceConditionsApiMock.list(POLICY.getId())).thenReturn(ImmutableList.of(ALERTS_CONDITION_UPDATED));
        when(alertsExternalServiceConditionsApiMock.update(ALERTS_CONDITION_UPDATED.getId(), ALERTS_CONDITION_FROM_CONFIG))
            .thenReturn(ALERTS_CONDITION_UPDATED);

        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsExternalServiceConditionsApiMock);
        order.verify(alertsExternalServiceConditionsApiMock).list(POLICY.getId());
        order.verify(alertsExternalServiceConditionsApiMock).update(ALERTS_CONDITION_UPDATED.getId(), ALERTS_CONDITION_FROM_CONFIG);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldRemoveOldCondition() {
        // given
        when(alertsExternalServiceConditionsApiMock.list(POLICY.getId()))
            .thenReturn(ImmutableList.of(ALERTS_CONDITION_DIFFERENT));
        when(alertsExternalServiceConditionsApiMock.create(POLICY.getId(), ALERTS_CONDITION_FROM_CONFIG))
            .thenReturn(ALERTS_CONDITION_SAME);

        // when
        testee.sync(CONFIGURATION);

        // then
        InOrder order = inOrder(alertsExternalServiceConditionsApiMock);
        order.verify(alertsExternalServiceConditionsApiMock).list(POLICY.getId());
        order.verify(alertsExternalServiceConditionsApiMock).create(POLICY.getId(), ALERTS_CONDITION_FROM_CONFIG);
        order.verify(alertsExternalServiceConditionsApiMock).delete(ALERTS_CONDITION_DIFFERENT.getId());
        order.verifyNoMoreInteractions();
    }

    private static PolicyConfiguration createConfiguration() {
        return PolicyConfiguration.builder()
            .policyName(POLICY_NAME)
            .incidentPreference(INCIDENT_PREFERENCE)
            .externalServiceCondition(EXTERNAL_SERVICE_CONDITION)
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

    private static ApmExternalServiceCondition createCondition(String conditionName) {
        return ApmExternalServiceCondition.builder()
            .conditionName(conditionName)
            .enabled(ENABLED)
            .application(APPLICATION_NAME)
            .metric(METRIC)
            .externalServiceUrl(EXTERNAL_SERVICE_URL)
            .term(TERMS_CONFIGURATION)
            .build();
    }

    private static AlertsExternalServiceCondition.AlertsExternalServiceConditionBuilder createConditionBuilder() {
        return AlertsExternalServiceCondition.builder()
            .type(ExternalServiceConditionType.APM.getTypeString())
            .name(CONDITION_NAME)
            .enabled(ENABLED)
            .entity(APPLICATION_ENTITY_ID)
            .metric(METRIC.name().toLowerCase())
            .externalServiceUrl(EXTERNAL_SERVICE_URL)
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
