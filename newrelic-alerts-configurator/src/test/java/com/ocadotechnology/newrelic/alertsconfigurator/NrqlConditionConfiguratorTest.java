package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.NrqlCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.NrqlDurationTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.NrqlTermsConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.OperatorTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.PriorityTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TimeFunctionTerm;
import com.ocadotechnology.newrelic.apiclient.PolicyItemApi;
import com.ocadotechnology.newrelic.apiclient.model.conditions.Terms;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.AlertsNrqlCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.Nrql;
import org.junit.Before;

public class NrqlConditionConfiguratorTest extends AbstractPolicyItemConfiguratorTest<NrqlConditionConfigurator, AlertsNrqlCondition> {

    private static final NrqlTermsConfiguration TERMS_CONFIGURATION = createTermsConfiguration().build();
    private static final NrqlCondition.ValueFunction VALUE_FUNCTION = NrqlCondition.ValueFunction.SINGLE_VALUE;
    private static final String QUERY = "query";
    private static final NrqlCondition NRQL_CONDITION = createNrqlCondition(CONDITION_NAME);

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

    private static NrqlTermsConfiguration.NrqlTermsConfigurationBuilder createTermsConfiguration() {
        return NrqlTermsConfiguration.builder()
                .durationTerm(NrqlDurationTerm.DURATION_1)
                .operatorTerm(OperatorTerm.ABOVE)
                .priorityTerm(PriorityTerm.CRITICAL)
                .thresholdTerm(0.5f)
                .timeFunctionTerm(TimeFunctionTerm.ALL);
    }

    private static NrqlCondition createNrqlCondition(String conditionName) {
        return NrqlCondition.builder()
                .conditionName(conditionName)
                .enabled(ENABLED)
                .term(TERMS_CONFIGURATION)
                .valueFunction(VALUE_FUNCTION)
                .query(QUERY)
                .build();
    }

    private static PolicyConfiguration createConfiguration() {
        return PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
                .nrqlCondition(NRQL_CONDITION)
                .build();
    }

    private static AlertsNrqlCondition.AlertsNrqlConditionBuilder createConditionBuilder() {
        return AlertsNrqlCondition.builder()
                .name(CONDITION_NAME)
                .enabled(ENABLED)
                .term(Terms.builder()
                        .duration(String.valueOf(TERMS_CONFIGURATION.getDurationTerm().getDuration()))
                        .operator(TERMS_CONFIGURATION.getOperatorTerm().name().toLowerCase())
                        .priority(TERMS_CONFIGURATION.getPriorityTerm().name().toLowerCase())
                        .threshold(String.valueOf(TERMS_CONFIGURATION.getThresholdTerm()))
                        .timeFunction(TERMS_CONFIGURATION.getTimeFunctionTerm().name().toLowerCase())
                        .build())
                .valueFunction(VALUE_FUNCTION.getValueString())
                .violationTimeLimitSeconds(NrqlCondition.DEFAULT_VIOLATION_TIME_LIMIT_SECONDS)
                .nrql(Nrql.builder()
                        .query(QUERY)
                        .build());
    }

}
