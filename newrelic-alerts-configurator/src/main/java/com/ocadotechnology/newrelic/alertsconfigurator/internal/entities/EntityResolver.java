package com.ocadotechnology.newrelic.alertsconfigurator.internal.entities;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.Condition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ConditionType;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ExternalServiceCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ExternalServiceConditionType;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.SyntheticsCondition;
import com.ocadotechnology.newrelic.apiclient.NewRelicApi;

public abstract class EntityResolver {
    private static final EntityResolver INSTANCE = new DefaultEntityResolver();

    public static EntityResolver defaultInstance() {
        return INSTANCE;
    }

    public Collection<Integer> resolveEntities(NewRelicApi api, Condition condition) {
        IdProvider idProvider = getIdProvider(condition.getType().getTypeString());
        return condition.getEntities().stream()
                .map(e -> idProvider.getId(api, e))
                .collect(Collectors.toList());
    }

    public Collection<Integer> resolveEntities(NewRelicApi api, ExternalServiceCondition condition) {
        IdProvider idProvider = getIdProvider(condition.getType().getTypeString());
        return condition.getEntities().stream()
                .map(e -> idProvider.getId(api, e))
                .collect(Collectors.toList());
    }

    public String resolveEntity(NewRelicApi api, SyntheticsCondition condition) {
        return getUuidProvider(SyntheticsCondition.class.getSimpleName()).getUuid(api, condition.getMonitorName());
    }

    protected abstract IdProvider getIdProvider(String conditionType);

    protected abstract UuidProvider getUuidProvider(String conditionType);

    private static final class DefaultEntityResolver extends EntityResolver {
        private final Map<String, IdProvider> idProviders = new HashMap<>();
        private final Map<String, UuidProvider> uuidProviders = new HashMap<>();

        DefaultEntityResolver() {
            idProviders.put(ConditionType.APM_APP.getTypeString(), new ApplicationIdProvider());
            idProviders.put(ConditionType.APM_KEY_TRANSACTION.getTypeString(), new KeyTransactionIdProvider());
            idProviders.put(ConditionType.SERVERS_METRIC.getTypeString(), new ServerIdProvider());
            idProviders.put(ExternalServiceConditionType.APM.getTypeString(), new ApplicationIdProvider());
            idProviders.put(ConditionType.APM_JVM.getTypeString(), new ApplicationIdProvider());
            idProviders.put(ConditionType.BROWSER_METRIC.getTypeString(), new ApplicationIdProvider());

            uuidProviders.put(SyntheticsCondition.class.getSimpleName(), new SyntheticsMonitorUuidProvider());
        }

        @Override
        public IdProvider getIdProvider(String conditionType) {
            IdProvider resolver = idProviders.get(conditionType);
            if (resolver == null) {
                throw new IllegalArgumentException("Cannot resolve entity for condition " + conditionType);
            }
            return resolver;
        }

        @Override
        protected UuidProvider getUuidProvider(String conditionType) {
            UuidProvider resolver = uuidProviders.get(conditionType);
            if (resolver == null) {
                throw new IllegalArgumentException("Cannot resolve entity for condition " + conditionType);
            }
            return resolver;
        }
    }
}
