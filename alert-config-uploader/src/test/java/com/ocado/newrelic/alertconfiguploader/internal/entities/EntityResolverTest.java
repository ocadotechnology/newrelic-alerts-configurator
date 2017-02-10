package com.ocado.newrelic.alertconfiguploader.internal.entities;

import com.ocado.newrelic.alertconfiguploader.configuration.condition.Condition;
import com.ocado.newrelic.alertconfiguploader.configuration.condition.ConditionType;
import com.ocado.newrelic.alertconfiguploader.configuration.condition.ExternalServiceCondition;
import com.ocado.newrelic.alertconfiguploader.configuration.condition.ExternalServiceConditionType;
import com.ocado.newrelic.api.NewRelicApi;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class EntityResolverTest {
    @Rule
    public final MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    private NewRelicApi apiMock;
    @Mock
    private IdProvider idProviderMock;
    @Spy
    private final EntityResolver testee = new EntityResolver() {
        @Override
        protected IdProvider getIdProvider(String conditionType) {
            return idProviderMock;
        }
    };

    @Test
    public void resolveEntities_shouldResolveConditionEntities() {
        // given
        Condition condition = mock(Condition.class);
        when(condition.getType()).thenReturn(ConditionType.APM_KEY_TRANSACTION);
        when(condition.getEntities()).thenReturn(Arrays.asList("e1", "e2"));

        when(idProviderMock.getId(apiMock, "e1")).thenReturn(1);
        when(idProviderMock.getId(apiMock, "e2")).thenReturn(2);

        // when
        Collection<Integer> ids = testee.resolveEntities(apiMock, condition);

        // then
        assertThat(ids).containsExactly(1, 2);
        verify(testee).getIdProvider(ConditionType.APM_KEY_TRANSACTION.getTypeString());
    }

    @Test
    public void resolveEntities_shouldResolveExternalServiceConditionEntities() {
        // given
        ExternalServiceCondition condition = mock(ExternalServiceCondition.class);
        when(condition.getType()).thenReturn(ExternalServiceConditionType.APM);
        when(condition.getEntities()).thenReturn(Arrays.asList("e1", "e2"));

        when(idProviderMock.getId(apiMock, "e1")).thenReturn(1);
        when(idProviderMock.getId(apiMock, "e2")).thenReturn(2);

        // when
        Collection<Integer> ids = testee.resolveEntities(apiMock, condition);

        // then
        assertThat(ids).containsExactly(1, 2);
        verify(testee).getIdProvider(ExternalServiceConditionType.APM.getTypeString());
    }

    @Test
    public void defaultInstance_shouldSupportAllConditionEntities() {
        for (ConditionType type : ConditionType.values()) {
            assertCanResolveType(type.getTypeString());
        }
    }

    @Test
    public void defaultInstance_shouldSupportAllExternalServiceCondtionEntities() {
        for (ExternalServiceConditionType type : ExternalServiceConditionType.values()) {
            assertCanResolveType(type.getTypeString());
        }
    }

    private void assertCanResolveType(String conditionType) {
        IdProvider provider = EntityResolver.defaultInstance().getIdProvider(conditionType);
        assertNotNull("Should resolve id for " + conditionType, provider);
    }
}
