package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.NrqlCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.lossofsignal.ExpirationConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.lossofsignal.SignalConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.lossofsignal.SignalConfiguration.FillOption;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.NrqlDurationTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.NrqlTermsConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.OperatorTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.PriorityTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TimeFunctionTerm;
import com.ocadotechnology.newrelic.apiclient.PolicyItemApi;
import com.ocadotechnology.newrelic.apiclient.model.conditions.Terms;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.AlertsNrqlCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.Expiration;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.Nrql;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.Signal;
import org.junit.Before;

import java.util.concurrent.TimeUnit;

public class NrqlConditionConfiguratorTest extends AbstractPolicyItemConfiguratorTest<NrqlConditionConfigurator, AlertsNrqlCondition> {

    private static final NrqlTermsConfiguration TERMS_CONFIGURATION = createTermsConfiguration().build();
    private static final NrqlCondition.ValueFunction VALUE_FUNCTION = NrqlCondition.ValueFunction.SINGLE_VALUE;
    private static final NrqlCondition.SinceValue SINCE_VALUE = NrqlCondition.SinceValue.SINCE_1;
    private static final String QUERY = "query";

    private static final FillOption FILL_OPTION = FillOption.STATIC;
    private static final String FILL_VALUE = "123";
    private static final int AGGREGATION_WINDOW_MINUTES = 3;
    private static final int EVALUATION_OFFSET_MINUTES = 5;

    private static final int EXPIRATION_DURATION_SECONDS = 10;
    private static final boolean OPEN_VIOLATION_ON_EXPIRATION = true;
    private static final boolean CLOSE_VIOLATIONS_ON_EXPIRATION = true;

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
                .sinceValue(SINCE_VALUE)
                .expiration(ExpirationConfiguration.builder()
                        .closeViolationsOnExpiration(CLOSE_VIOLATIONS_ON_EXPIRATION)
                        .openViolationOnExpiration(OPEN_VIOLATION_ON_EXPIRATION)
                        .expirationDuration(EXPIRATION_DURATION_SECONDS, TimeUnit.SECONDS)
                        .build())
                .signal(SignalConfiguration.builder()
                        .aggregationWindow(AGGREGATION_WINDOW_MINUTES, TimeUnit.MINUTES)
                        .evaluationOffset(EVALUATION_OFFSET_MINUTES, TimeUnit.MINUTES)
                        .fillOption(FILL_OPTION)
                        .fillValue(FILL_VALUE)
                        .build())
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
                .nrql(Nrql.builder()
                        .query(QUERY)
                        .sinceValue(String.valueOf(SINCE_VALUE.getSince()))
                        .build())
                .signal(Signal.builder()
                        .fillOption(FILL_OPTION.getValue())
                        .fillValue(FILL_VALUE)
                        .aggregationWindow(String.valueOf(AGGREGATION_WINDOW_MINUTES))
                        .evaluationOffset(String.valueOf(EVALUATION_OFFSET_MINUTES))
                        .build())
                .expiration(Expiration.builder()
                        .expirationDuration(String.valueOf(EXPIRATION_DURATION_SECONDS))
                        .openViolationOnExpiration(OPEN_VIOLATION_ON_EXPIRATION)
                        .closeViolationsOnExpiration(CLOSE_VIOLATIONS_ON_EXPIRATION)
                        .build());
    }

}
