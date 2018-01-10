package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ExternalServiceCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsUtils;
import com.ocadotechnology.newrelic.alertsconfigurator.internal.entities.EntityResolver;
import com.ocadotechnology.newrelic.apiclient.NewRelicApi;
import com.ocadotechnology.newrelic.apiclient.PolicyItemApi;
import com.ocadotechnology.newrelic.apiclient.model.conditions.external.AlertsExternalServiceCondition;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

@Slf4j
class ExternalServiceConditionConfigurator extends AbstractPolicyItemConfigurator<AlertsExternalServiceCondition, ExternalServiceCondition> {
    private final EntityResolver entityResolver;

    ExternalServiceConditionConfigurator(@NonNull NewRelicApi api, @NonNull EntityResolver entityResolver) {
        super(api);
        this.entityResolver = entityResolver;
    }

    @Override
    protected Collection<ExternalServiceCondition> getConfigItems(PolicyConfiguration config) {
        return config.getExternalServiceConditions();
    }

    @Override
    protected AlertsExternalServiceCondition convertFromConfigItem(ExternalServiceCondition condition) {
        return AlertsExternalServiceCondition.builder()
            .type(condition.getTypeString())
            .name(condition.getConditionName())
            .enabled(condition.isEnabled())
            .entities(entityResolver.resolveEntities(api, condition))
            .metric(condition.getMetric())
            .externalServiceUrl(condition.getExternalServiceUrl())
            .runbookUrl(condition.getRunBookUrl())
            .terms(TermsUtils.createTerms(condition.getTerms()))
            .build();
    }

    @Override
    protected boolean sameInstance(AlertsExternalServiceCondition alertsCondition1, AlertsExternalServiceCondition alertsCondition2) {
        return StringUtils.equals(alertsCondition1.getName(), alertsCondition2.getName())
            && StringUtils.equals(alertsCondition1.getType(), alertsCondition2.getType());
    }

    @Override
    protected PolicyItemApi<AlertsExternalServiceCondition> getItemsApi() {
        return api.getAlertsExternalServiceConditionsApi();
    }
}
