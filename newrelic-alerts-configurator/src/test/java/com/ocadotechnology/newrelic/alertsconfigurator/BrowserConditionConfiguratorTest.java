package com.ocadotechnology.newrelic.alertsconfigurator;

import com.google.common.collect.ImmutableList;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.BrowserCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.Condition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.DurationTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.OperatorTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.PriorityTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TimeFunctionTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.internal.entities.EntityResolver;
import com.ocadotechnology.newrelic.apiclient.model.conditions.AlertsCondition;
import com.ocadotechnology.newrelic.apiclient.model.policies.AlertsPolicy;
import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BrowserConditionConfiguratorTest extends AbstractConfiguratorTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    private static final String POLICY_NAME = "policyName";
    private static final String CONDITION_NAME = "Page views with JS errors (High)";
    private static final boolean ENABLED = true;
    private static final String APPLICATION_NAME = "applicationName";
    private static final int APPLICATION_ENTITY_ID = 1;

    private static final AlertsPolicy POLICY = AlertsPolicy.builder().id(42).name(POLICY_NAME).build();
    private static final TermsConfiguration TERMS_CONFIGURATION = createTermsConfiguration().build();
    private static final Condition APP_CONDITION = createBrowserCondition(CONDITION_NAME);
    private static final PolicyConfiguration CONFIGURATION = createConfiguration();

    @Captor
    private ArgumentCaptor<AlertsCondition> alertsConditionCaptor;
    @Mock
    private EntityResolver entityResolverMock;

    @InjectMocks
    private ConditionConfigurator testee;

    @Before
    public void setUp() {
        when(alertsPoliciesApiMock.getByName(POLICY_NAME)).thenReturn(Optional.of(POLICY));
        when(entityResolverMock.resolveEntities(apiMock, APP_CONDITION)).thenReturn(Collections.singletonList(APPLICATION_ENTITY_ID));
    }

    @Test
    public void shouldCorrectlyCreateBrowserCondition() throws Exception {
        //given
        when(alertsConditionsApiMock.list(POLICY.getId())).thenReturn(ImmutableList.of());
        when(alertsConditionsApiMock.create(eq(POLICY.getId()), any(AlertsCondition.class))).thenReturn(AlertsCondition.builder().build());

        //when
        testee.sync(CONFIGURATION);

        //then
        verify(alertsConditionsApiMock).create(eq(POLICY.getId()), alertsConditionCaptor.capture());
        AlertsCondition result = alertsConditionCaptor.getValue();
        assertThat(result.getType()).isEqualTo("browser_metric");
        assertThat(result.getName()).isEqualTo(CONDITION_NAME);
        assertThat(result.getEnabled()).isEqualTo(true);
        assertThat(result.getEntities()).containsExactly(APPLICATION_ENTITY_ID);
        assertThat(result.getMetric()).isEqualTo("page_views_with_js_errors");
        assertThat(result.getTerms())
                .extracting("duration", "operator", "priority", "threshold", "timeFunction")
                .containsExactly(new Tuple("10", "above", "warning", "1.0", "any"));
    }

    private static PolicyConfiguration createConfiguration() {
        return PolicyConfiguration.builder()
            .policyName(POLICY_NAME)
            .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
            .condition(APP_CONDITION)
            .build();
    }

    private static TermsConfiguration.TermsConfigurationBuilder createTermsConfiguration() {
        return TermsConfiguration.builder()
            .durationTerm(DurationTerm.DURATION_10)
            .operatorTerm(OperatorTerm.ABOVE)
            .priorityTerm(PriorityTerm.WARNING)
            .timeFunctionTerm(TimeFunctionTerm.ANY)
            .thresholdTerm(1f);
    }

    private static BrowserCondition createBrowserCondition(String conditionName) {
        return BrowserCondition.builder()
            .conditionName(conditionName)
            .enabled(ENABLED)
            .application(APPLICATION_NAME)
            .metric(BrowserCondition.Metric.PAGE_VIEWS_WITH_JS_ERRORS)
            .term(TERMS_CONFIGURATION)
            .build();
    }
}
