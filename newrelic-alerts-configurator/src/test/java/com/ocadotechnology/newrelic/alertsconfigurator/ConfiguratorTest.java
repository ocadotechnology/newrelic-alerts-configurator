package com.ocadotechnology.newrelic.alertsconfigurator;

import com.google.common.collect.ImmutableList;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.ApplicationConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.DashboardConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ConfiguratorTest {

    @Mock
    private DashboardConfigurator dashboardConfiguratorMock;
    @Mock
    private ApplicationConfigurator applicationConfiguratorMock;
    @Mock
    private PolicyConfigurator policyConfiguratorMock;
    @Mock
    private ConditionConfigurator conditionConfiguratorMock;
    @Mock
    private ExternalServiceConditionConfigurator externalServiceConditionConfiguratorMock;
    @Mock
    private NrqlConditionConfigurator nrqlConditionConfiguratorMock;
    @Mock
    private SyntheticsConditionConfigurator syntheticsConditionConfiguratorMock;
    @Mock
    private ChannelConfigurator channelConfiguratorMock;

    @Mock
    private ApplicationConfiguration applicationConfigurationMock;
    @Mock
    private PolicyConfiguration policyConfigurationMock;
    @Mock
    private DashboardConfiguration dashboardConfigurationMock;


    private Configurator testee;

    @Before
    public void setUp() {
        testee = new Configurator(applicationConfiguratorMock,
                policyConfiguratorMock,
                conditionConfiguratorMock,
                externalServiceConditionConfiguratorMock,
                nrqlConditionConfiguratorMock,
                syntheticsConditionConfiguratorMock,
                channelConfiguratorMock,
                dashboardConfiguratorMock);
    }


    @Test
    public void shouldThrowException_whenNoApiKey() {
        // given

        // when
        Throwable exception = catchThrowable(() -> new Configurator(null));

        // then - exception
        assertThat(exception).isInstanceOf(NullPointerException.class);
        assertThat(exception).hasMessage("apiKey");
    }

    @Test
    public void shouldNotSynchronizeAnything_whenNoConfigurationsSet() {
        // given

        // when
        testee.sync();

        // then
        InOrder order = inOrder(applicationConfiguratorMock,
                policyConfiguratorMock,
                conditionConfiguratorMock,
                externalServiceConditionConfiguratorMock,
                nrqlConditionConfiguratorMock,
                syntheticsConditionConfiguratorMock,
                channelConfiguratorMock,
                dashboardConfiguratorMock);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldSynchronizeMoreThanOneConfigurations_whenMoreThanOneConfigurationsSet() {
        // given
        ApplicationConfiguration applicationConfigurationMock2 = mock(ApplicationConfiguration.class);
        PolicyConfiguration policyConfigurationMock2 = mock(PolicyConfiguration.class);
        DashboardConfiguration dashboardConfigurationMock2 = mock(DashboardConfiguration.class);
        testee.setApplicationConfigurations(ImmutableList.of(applicationConfigurationMock, applicationConfigurationMock2));
        testee.setPolicyConfigurations(ImmutableList.of(policyConfigurationMock, policyConfigurationMock2));
        testee.setDashboardConfigurations(ImmutableList.of(dashboardConfigurationMock, dashboardConfigurationMock2));

        // when
        testee.sync();

        // then
        InOrder order = inOrder(applicationConfiguratorMock,
                policyConfiguratorMock,
                conditionConfiguratorMock,
                externalServiceConditionConfiguratorMock,
                nrqlConditionConfiguratorMock,
                syntheticsConditionConfiguratorMock,
                channelConfiguratorMock,
                dashboardConfiguratorMock);
        order.verify(applicationConfiguratorMock).sync(applicationConfigurationMock);
        order.verify(applicationConfiguratorMock).sync(applicationConfigurationMock2);
        order.verify(policyConfiguratorMock).sync(policyConfigurationMock);
        order.verify(policyConfiguratorMock).sync(policyConfigurationMock2);
        order.verify(dashboardConfiguratorMock).sync(dashboardConfigurationMock);
        order.verify(dashboardConfiguratorMock).sync(dashboardConfigurationMock2);
    }

    @Test
    public void shouldSynchronizeAllConfigurationsForPolicy_whenFullPolicyConfiguration() {
        // given
        testee.setPolicyConfigurations(ImmutableList.of(policyConfigurationMock));

        // when
        testee.sync();

        // then
        InOrder order = inOrder(applicationConfiguratorMock,
                policyConfiguratorMock,
                conditionConfiguratorMock,
                externalServiceConditionConfiguratorMock,
                nrqlConditionConfiguratorMock,
                syntheticsConditionConfiguratorMock,
                channelConfiguratorMock);
        order.verify(policyConfiguratorMock).sync(policyConfigurationMock);
        order.verify(conditionConfiguratorMock).sync(policyConfigurationMock);
        order.verify(externalServiceConditionConfiguratorMock).sync(policyConfigurationMock);
        order.verify(nrqlConditionConfiguratorMock).sync(policyConfigurationMock);
        order.verify(syntheticsConditionConfiguratorMock).sync(policyConfigurationMock);
        order.verify(channelConfiguratorMock).sync(policyConfigurationMock);
        order.verifyNoMoreInteractions();
    }
}
