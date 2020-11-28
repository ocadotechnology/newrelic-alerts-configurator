package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.NrqlCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.lossofsignal.LossOfSignalUtils;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsUtils;
import com.ocadotechnology.newrelic.apiclient.NewRelicApi;
import com.ocadotechnology.newrelic.apiclient.PolicyItemApi;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.AlertsNrqlCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.Nrql;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Optional;

@Slf4j
class NrqlConditionConfigurator extends AbstractPolicyItemConfigurator<AlertsNrqlCondition, NrqlCondition> {

    NrqlConditionConfigurator(@NonNull NewRelicApi api) {
        super(api);
    }

    @Override
    protected Optional<Collection<NrqlCondition>> getConfigItems(PolicyConfiguration config) {
        return config.getNrqlConditions();
    }

    @Override
    protected PolicyItemApi<AlertsNrqlCondition> getItemsApi() {
        return api.getAlertsNrqlConditionsApi();
    }

    @Override
    protected AlertsNrqlCondition convertFromConfigItem(NrqlCondition condition) {
        return AlertsNrqlCondition.builder()
                .name(condition.getConditionName())
                .enabled(condition.isEnabled())
                .runbookUrl(condition.getRunBookUrl())
                .terms(TermsUtils.createNrqlTerms(condition.getTerms()))
                .valueFunction(condition.getValueFunction().getValueString())
                .nrql(Nrql.builder()
                        .sinceValue(String.valueOf(condition.getSinceValue().getSince()))
                        .query(condition.getQuery())
                        .build())
                .expiration(LossOfSignalUtils.createExpiration(condition.getExpiration()))
                .signal(LossOfSignalUtils.createSignal(condition.getSignal()))
                .build();
    }

    @Override
    protected boolean sameInstance(AlertsNrqlCondition alertsNrqlCondition1, AlertsNrqlCondition alertsNrqlCondition2) {
        return StringUtils.equals(alertsNrqlCondition1.getName(), alertsNrqlCondition2.getName());
    }
}
