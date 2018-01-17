package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ApmExternalServiceCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ExternalServiceCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ExternalServiceConditionType;
import com.ocadotechnology.newrelic.apiclient.PolicyItemApi;
import com.ocadotechnology.newrelic.apiclient.model.conditions.Terms;
import com.ocadotechnology.newrelic.apiclient.model.conditions.external.AlertsExternalServiceCondition;
import org.junit.Before;

import java.util.Collections;
import static org.mockito.Mockito.when;

public class ExternalServiceConditionConfiguratorTest extends AbstractPolicyItemConfiguratorTest<ExternalServiceConditionConfigurator, AlertsExternalServiceCondition> {


    private static final ApmExternalServiceCondition.Metric METRIC = ApmExternalServiceCondition.Metric.RESPONSE_TIME_AVERAGE;
    private static final String EXTERNAL_SERVICE_URL = "externalServiceUrl";
    private static final ExternalServiceCondition EXTERNAL_SERVICE_CONDITION = createCondition(CONDITION_NAME);
    private static final AlertsExternalServiceCondition ALERTS_CONDITION_SAME = createConditionBuilder().id(1).build();
    private static final AlertsExternalServiceCondition ALERTS_CONDITION_FROM_CONFIG = createConditionBuilder().build();
    private static final AlertsExternalServiceCondition ALERTS_CONDITION_UPDATED = createConditionBuilder().id(2).enabled(!ENABLED).build();
    private static final AlertsExternalServiceCondition ALERTS_CONDITION_DIFFERENT = createConditionBuilder().id(3).name("different").build();

    private static final PolicyConfiguration CONFIGURATION = createConfiguration();

    @Before
    public void setUp() {
        super.setUp();
        when(entityResolverMock.resolveEntities(apiMock, EXTERNAL_SERVICE_CONDITION)).thenReturn(Collections.singletonList(APPLICATION_ENTITY_ID));
        testee = new ExternalServiceConditionConfigurator(apiMock, entityResolverMock);
    }

    @Override
    protected PolicyConfiguration getPolicyConfiguration() {
        return CONFIGURATION;
    }

    @Override
    protected PolicyItemApi<AlertsExternalServiceCondition> getPolicyItemApiMock() {
        return alertsExternalServiceConditionsApiMock;
    }

    @Override
    protected AlertsExternalServiceCondition getItemFromConfig() {
        return ALERTS_CONDITION_FROM_CONFIG;
    }

    @Override
    protected AlertsExternalServiceCondition getItemSame() {
        return ALERTS_CONDITION_SAME;
    }

    @Override
    protected AlertsExternalServiceCondition getItemUpdated() {
        return ALERTS_CONDITION_UPDATED;
    }

    @Override
    protected AlertsExternalServiceCondition getItemDifferent() {
        return ALERTS_CONDITION_DIFFERENT;
    }

    private static PolicyConfiguration createConfiguration() {
        return PolicyConfiguration.builder()
            .policyName(POLICY_NAME)
            .incidentPreference(INCIDENT_PREFERENCE)
            .externalServiceCondition(EXTERNAL_SERVICE_CONDITION)
            .build();
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
