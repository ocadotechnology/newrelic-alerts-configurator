package com.ocadotechnology.newrelic.alertsconfigurator.internal.entities;


import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.Condition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ConditionType;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ExternalServiceCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ExternalServiceConditionType;
import com.ocadotechnology.newrelic.apiclient.NewRelicApi;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    protected abstract IdProvider getIdProvider(String conditionType);

    private static final class DefaultEntityResolver extends EntityResolver {
        private final Map<String, IdProvider> providers = new HashMap<>();

        public DefaultEntityResolver() {
            providers.put(ConditionType.APM_APP.getTypeString(), new ApplicationIdProvider());
            providers.put(ConditionType.APM_KEY_TRANSACTION.getTypeString(), new KeyTransactionIdProvider());
            providers.put(ConditionType.SERVERS_METRIC.getTypeString(), new ServerIdProvider());
            providers.put(ExternalServiceConditionType.APM.getTypeString(), new ApplicationIdProvider());
        }

        @Override
        public IdProvider getIdProvider(String conditionType) {
            IdProvider resolver = providers.get(conditionType);
            if (resolver == null) {
                throw new IllegalArgumentException("Cannot resolve entity for condition " + conditionType);
            }
            return resolver;
        }
    }
}
