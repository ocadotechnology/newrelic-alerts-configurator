package com.ocado.newrelic.alertconfiguploader;

import com.google.common.collect.ImmutableList;
import com.ocado.newrelic.alertconfiguploader.configuration.ApplicationConfiguration;
import com.ocado.newrelic.alertconfiguploader.configuration.PolicyConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SynchronizerTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Mock
    private ApplicationSynchronizer applicationSynchronizerMock;
    @Mock
    private PolicySynchronizer policySynchronizerMock;
    @Mock
    private ConditionSynchronizer conditionSynchronizerMock;
    @Mock
    private ExternalServiceConditionSynchronizer externalServiceConditionSynchronizerMock;
    @Mock
    private ChannelSynchronizer channelSynchronizerMock;

    @Mock
    private ApplicationConfiguration applicationConfigurationMock;
    @Mock
    private PolicyConfiguration policyConfigurationMock;


    private Synchronizer testee;

    @Before
    public void setUp() {
        testee = new Synchronizer(applicationSynchronizerMock,
            policySynchronizerMock,
            conditionSynchronizerMock,
            externalServiceConditionSynchronizerMock,
            channelSynchronizerMock);
    }


    @Test
    public void shouldThrowException_whenNoApiKey() {
        // given

        // then - exception
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("apiKey");

        // when
        new Synchronizer(null);
    }

    @Test
    public void shouldNotSynchronizeAnything_whenNoConfigurationsSet() {
        // given

        // when
        testee.sync();

        // then
        InOrder order = inOrder(applicationSynchronizerMock,
            policySynchronizerMock,
            conditionSynchronizerMock,
            externalServiceConditionSynchronizerMock,
            channelSynchronizerMock);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void shouldSynchronizeMoreThanOneConfigurations_whenMoreThanOneConfigurationsSet() {
        // given
        ApplicationConfiguration applicationConfigurationMock2 = mock(ApplicationConfiguration.class);
        PolicyConfiguration policyConfigurationMock2 = mock(PolicyConfiguration.class);
        testee.setApplicationConfigurations(ImmutableList.of(applicationConfigurationMock, applicationConfigurationMock2));
        testee.setPolicyConfigurations(ImmutableList.of(policyConfigurationMock, policyConfigurationMock2));

        // when
        testee.sync();

        // then
        InOrder order = inOrder(applicationSynchronizerMock,
            policySynchronizerMock,
            conditionSynchronizerMock,
            externalServiceConditionSynchronizerMock,
            channelSynchronizerMock);
        order.verify(applicationSynchronizerMock).sync(applicationConfigurationMock);
        order.verify(applicationSynchronizerMock).sync(applicationConfigurationMock2);
        order.verify(policySynchronizerMock).sync(policyConfigurationMock);
        order.verify(policySynchronizerMock).sync(policyConfigurationMock2);
    }

    @Test
    public void shouldSynchronizeAllConfigurationsForPolicy_whenFullPolicyConfiguration() {
        // given
        testee.setPolicyConfigurations(ImmutableList.of(policyConfigurationMock));

        // when
        testee.sync();

        // then
        InOrder order = inOrder(applicationSynchronizerMock,
            policySynchronizerMock,
            conditionSynchronizerMock,
            externalServiceConditionSynchronizerMock,
            channelSynchronizerMock);
        order.verify(policySynchronizerMock).sync(policyConfigurationMock);
        order.verify(conditionSynchronizerMock).sync(policyConfigurationMock);
        order.verify(externalServiceConditionSynchronizerMock).sync(policyConfigurationMock);
        order.verify(channelSynchronizerMock).sync(policyConfigurationMock);
        order.verifyNoMoreInteractions();
    }
}
