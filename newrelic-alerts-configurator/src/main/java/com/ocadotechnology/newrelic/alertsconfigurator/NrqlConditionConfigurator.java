package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql.NrqlCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql.NrqlUtils;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsUtils;
import com.ocadotechnology.newrelic.apiclient.NewRelicApi;

import com.ocadotechnology.newrelic.apiclient.PolicyItemApi;
import com.ocadotechnology.newrelic.apiclient.model.conditions.AlertsNrqlCondition;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

@Slf4j
class NrqlConditionConfigurator extends AbstractPolicyItemConfigurator<AlertsNrqlCondition, NrqlCondition> {

    NrqlConditionConfigurator(@NonNull NewRelicApi api) {
        super(api);
    }

    @Override
    protected Collection<NrqlCondition> getConfigItems(PolicyConfiguration config) {
        return config.getNrqlConditions();
    }

    @Override
    protected PolicyItemApi<AlertsNrqlCondition> getItemsApi() {
        return api.getAlertsNrqlConditionsApi();
    }

    @Override
    protected AlertsNrqlCondition convertFromConfigItem(NrqlCondition configItem) {
        return AlertsNrqlCondition.builder()
                .name(configItem.getConditionName())
                .enabled(configItem.isEnabled())
                .runbookUrl(configItem.getRunBookUrl())
                .terms(TermsUtils.createTerms(configItem.getTerms()))
                .valueFunction(configItem.getValueFunction().getAsString())
                .nrql(NrqlUtils.mapNrql(configItem.getNrql()))
                .build();
    }

    @Override
    protected boolean sameInstance(AlertsNrqlCondition alertsNrqlCondition1, AlertsNrqlCondition alertsNrqlCondition2) {
        return StringUtils.equals(alertsNrqlCondition1.getName(), alertsNrqlCondition2.getName());
    }
}
