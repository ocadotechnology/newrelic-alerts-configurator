package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql.NrqlCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql.NrqlConfiguration;
import com.ocadotechnology.newrelic.apiclient.PolicyItemApi;
import com.ocadotechnology.newrelic.apiclient.model.conditions.AlertsNrqlCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.Nrql;
import com.ocadotechnology.newrelic.apiclient.model.conditions.Terms;
import org.junit.Before;

import static com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql.SinceValue.SINCE_1_MINUTE;

public class NrqlConditionConfiguratorTest extends AbstractPolicyItemConfiguratorTest<NrqlConditionConfigurator, AlertsNrqlCondition> {
    private static final String SUM = "sum";
    private static final String NRQL = "SELECT COUNT(*) FROM Transaction";

    private static final AlertsNrqlCondition ALERTS_CONDITION_SAME = createConditionBuilder().id(1).build();
    private static final AlertsNrqlCondition ALERTS_CONDITION_FROM_CONFIG = createConditionBuilder().build();
    private static final AlertsNrqlCondition ALERTS_CONDITION_UPDATED = createConditionBuilder().id(2).enabled(!ENABLED).build();
    private static final AlertsNrqlCondition ALERTS_CONDITION_DIFFERENT = createConditionBuilder().id(3).name("different").build();

    private static final PolicyConfiguration CONFIGURATION = createConfiguration();


    @Override
    protected PolicyConfiguration getPolicyConfiguration() {
        return CONFIGURATION;
    }

    @Override
    protected PolicyItemApi<AlertsNrqlCondition> getPolicyItemApiMock() {
        return alertsNrqlConditionsApiMock;
    }

    @Override
    protected AlertsNrqlCondition getItemFromConfig() {
        return ALERTS_CONDITION_FROM_CONFIG;
    }

    @Override
    protected AlertsNrqlCondition getItemSame() {
        return ALERTS_CONDITION_SAME;
    }

    @Override
    protected AlertsNrqlCondition getItemUpdated() {
        return ALERTS_CONDITION_UPDATED;
    }

    @Override
    protected AlertsNrqlCondition getItemDifferent() {
        return ALERTS_CONDITION_DIFFERENT;
    }

    @Before
    public void setUp() {
        super.setUp();
        testee = new NrqlConditionConfigurator(apiMock);
    }


    private static PolicyConfiguration createConfiguration() {
        return PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
                .nrqlCondition(createNrqlCondition())
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
