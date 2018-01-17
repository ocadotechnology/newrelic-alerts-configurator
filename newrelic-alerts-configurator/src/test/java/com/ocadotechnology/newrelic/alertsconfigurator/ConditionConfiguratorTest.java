package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ApmAppCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.Condition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ConditionType;
import com.ocadotechnology.newrelic.apiclient.PolicyItemApi;
import com.ocadotechnology.newrelic.apiclient.model.conditions.AlertsCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.Terms;
import org.junit.Before;

import java.util.Collections;

import static org.mockito.Mockito.when;


public class ConditionConfiguratorTest extends AbstractPolicyItemConfiguratorTest<ConditionConfigurator, AlertsCondition> {

    private static final ApmAppCondition.Metric APP_METRIC = ApmAppCondition.Metric.APDEX;

    static final Condition APP_CONDITION = createAppCondition(CONDITION_NAME);
    private static final AlertsCondition ALERTS_CONDITION_SAME = createConditionBuilder().id(1).build();
    private static final AlertsCondition ALERTS_CONDITION_FROM_CONFIG = createConditionBuilder().build();
    private static final AlertsCondition ALERTS_CONDITION_UPDATED = createConditionBuilder().id(2).enabled(!ENABLED).build();
    private static final AlertsCondition ALERTS_CONDITION_DIFFERENT = createConditionBuilder().id(3).name("different").build();

    private static final PolicyConfiguration CONFIGURATION = createConfiguration();


    @Before
    public void setUp() {
        super.setUp();
        when(entityResolverMock.resolveEntities(apiMock, APP_CONDITION)).thenReturn(Collections.singletonList(APPLICATION_ENTITY_ID));
        testee = new ConditionConfigurator(apiMock, entityResolverMock);
    }

    @Override
    protected PolicyConfiguration getPolicyConfiguration() {
        return CONFIGURATION;
    }

    @Override
    protected PolicyItemApi<AlertsCondition> getPolicyItemApiMock() {
        return alertsConditionsApiMock;
    }

    @Override
    protected AlertsCondition getItemFromConfig() {
        return ALERTS_CONDITION_FROM_CONFIG;
    }

    @Override
    protected AlertsCondition getItemSame() {
        return ALERTS_CONDITION_SAME;
    }

    @Override
    protected AlertsCondition getItemUpdated() {
        return ALERTS_CONDITION_UPDATED;
    }

    @Override
    protected AlertsCondition getItemDifferent() {
        return ALERTS_CONDITION_DIFFERENT;
    }

    private static PolicyConfiguration createConfiguration() {
        return PolicyConfiguration.builder()
            .policyName(POLICY_NAME)
            .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
            .condition(APP_CONDITION)
            .build();
    }

    private static AlertsCondition.AlertsConditionBuilder createConditionBuilder() {
        return AlertsCondition.builder()
            .type(ConditionType.APM_APP.getTypeString())
            .name(CONDITION_NAME)
            .enabled(ENABLED)
            .entity(APPLICATION_ENTITY_ID)
            .metric(APP_METRIC.name().toLowerCase())
            .conditionScope(CONDITION_SCOPE.name().toLowerCase())
            .violationCloseTimer(VIOLATION_CLOSE_TIMER.getDuration())
            .term(Terms.builder()
                    .duration(String.valueOf(TERMS_CONFIGURATION.getDurationTerm().getDuration()))
                    .operator(TERMS_CONFIGURATION.getOperatorTerm().name().toLowerCase())
                    .priority(TERMS_CONFIGURATION.getPriorityTerm().name().toLowerCase())
                    .threshold(String.valueOf(TERMS_CONFIGURATION.getThresholdTerm()))
                    .timeFunction(TERMS_CONFIGURATION.getTimeFunctionTerm().name().toLowerCase())
                    .build());
    }
}
