package com.ocadotechnology.newrelic.alertsconfigurator;

import java.util.Collection;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.SyntheticsCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.internal.entities.EntityResolver;
import com.ocadotechnology.newrelic.apiclient.NewRelicApi;
import com.ocadotechnology.newrelic.apiclient.PolicyItemApi;
import com.ocadotechnology.newrelic.apiclient.model.conditions.synthetics.AlertsSyntheticsCondition;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class SyntheticsConditionConfigurator extends AbstractPolicyItemConfigurator<AlertsSyntheticsCondition, SyntheticsCondition> {

    private final EntityResolver entityResolver;

    SyntheticsConditionConfigurator(@NonNull NewRelicApi api, @NonNull EntityResolver entityResolver) {
        super(api);
        this.entityResolver = entityResolver;
    }

    @Override
    protected Optional<Collection<SyntheticsCondition>> getConfigItems(PolicyConfiguration config) {
        return config.getSyntheticsConditions();
    }

    @Override
    protected PolicyItemApi<AlertsSyntheticsCondition> getItemsApi() {
        return api.getAlertsSyntheticsConditionApi();
    }

    @Override
    protected AlertsSyntheticsCondition convertFromConfigItem(SyntheticsCondition condition) {
        return AlertsSyntheticsCondition.builder()
                .name(condition.getConditionName())
                .monitorId(entityResolver.resolveEntity(api, condition))
                .enabled(condition.isEnabled())
                .runbookUrl(condition.getRunBookUrl())
                .build();
    }

    @Override
    protected boolean sameInstance(AlertsSyntheticsCondition alertsSyntheticsCondition1, AlertsSyntheticsCondition alertsSyntheticsCondition2) {
        return StringUtils.equals(alertsSyntheticsCondition1.getName(), alertsSyntheticsCondition2.getName());
    }
}
