package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.NrqlCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal.ExpirationUtils;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal.NrqlSignalConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal.SignalUtils;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsUtils;
import com.ocadotechnology.newrelic.apiclient.NewRelicApi;
import com.ocadotechnology.newrelic.apiclient.PolicyItemApi;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.AlertsNrqlCondition;
import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.Nrql;
import java.util.Collection;
import java.util.Optional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import static com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal.SignalUtils.intToStringOrNull;

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
        AlertsNrqlCondition.AlertsNrqlConditionBuilder nrqlConditionBuilder = AlertsNrqlCondition.builder()
            .name(condition.getConditionName())
            .enabled(condition.isEnabled())
            .runbookUrl(condition.getRunBookUrl())
            .terms(TermsUtils.createNrqlTerms(condition.getTerms()))
            .valueFunction(condition.getValueFunction().getValueString())
            .nrql(Nrql.builder()
                .sinceValue(getSinceValueOrNull(condition))
                .query(condition.getQuery())
                .build());

        NrqlSignalConfiguration nrqlSignalConfiguration = condition.getSignal();
        if (nrqlSignalConfiguration != null) {
            nrqlConditionBuilder
                .signal(SignalUtils.createSignal(nrqlSignalConfiguration))
                .expiration(ExpirationUtils.createExpiration(nrqlSignalConfiguration.getSignalLostConfiguration()));
        }

        return nrqlConditionBuilder.build();
    }

    private String getSinceValueOrNull(NrqlCondition condition) {
        return condition.getSinceValue() != null ? intToStringOrNull(condition.getSinceValue().getSince()) : null;
    }

    @Override
    protected boolean sameInstance(AlertsNrqlCondition alertsNrqlCondition1, AlertsNrqlCondition alertsNrqlCondition2) {
        return StringUtils.equals(alertsNrqlCondition1.getName(), alertsNrqlCondition2.getName());
    }
}
