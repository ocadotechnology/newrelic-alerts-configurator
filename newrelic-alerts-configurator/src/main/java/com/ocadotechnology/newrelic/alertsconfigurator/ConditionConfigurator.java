package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.Condition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsUtils;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.UserDefinedUtils;
import com.ocadotechnology.newrelic.alertsconfigurator.internal.entities.EntityResolver;
import com.ocadotechnology.newrelic.apiclient.NewRelicApi;
import com.ocadotechnology.newrelic.apiclient.PolicyItemApi;
import com.ocadotechnology.newrelic.apiclient.model.conditions.AlertsCondition;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

@Slf4j
class ConditionConfigurator extends AbstractPolicyItemConfigurator<AlertsCondition, Condition> {
    private final EntityResolver entityResolver;

    ConditionConfigurator(@NonNull NewRelicApi api, @NonNull EntityResolver entityResolver) {
        super(api);
        this.entityResolver = entityResolver;
    }

    @Override
    protected Collection<Condition> getConfigItems(PolicyConfiguration config) {
        return config.getConditions();
    }

    @Override
    protected AlertsCondition convertFromConfigItem(Condition condition) {
        return AlertsCondition.builder()
            .type(condition.getType().getTypeString())
            .name(condition.getConditionName())
            .enabled(condition.isEnabled())
            .entities(entityResolver.resolveEntities(api, condition))
            .metric(condition.getMetricAsString())
            .conditionScope(condition.getConditionScopeAsString())
            .runbookUrl(condition.getRunBookUrl())
            .terms(TermsUtils.createTerms(condition.getTerms()))
            .violationCloseTimer(condition.getViolationCloseTimerAsString())
            .userDefined(UserDefinedUtils.createUserDefined(condition.getUserDefinedMetric()))
            .gcMetric(condition.getGcMetricAsString())
            .build();
    }

    @Override
    protected boolean sameInstance(AlertsCondition alertsCondition1, AlertsCondition alertsCondition2) {
        return StringUtils.equals(alertsCondition1.getName(), alertsCondition2.getName())
            && StringUtils.equals(alertsCondition1.getType(), alertsCondition2.getType());
    }

    @Override
    protected PolicyItemApi<AlertsCondition> getItemsApi() {
        return api.getAlertsConditionsApi();
    }
}
