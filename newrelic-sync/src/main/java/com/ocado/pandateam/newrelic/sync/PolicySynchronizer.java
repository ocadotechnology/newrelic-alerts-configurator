package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.sync.configuration.PolicyConfiguration;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

class PolicySynchronizer {

    private final NewRelicApi api;
    private final PolicyConfiguration config;

    PolicySynchronizer(@NonNull NewRelicApi api, @NonNull PolicyConfiguration config) {
        this.config = config;
        this.api = api;
    }

    void sync() throws NewRelicApiException, NewRelicSyncException {
        AlertsPolicy configAlertPolicy = AlertsPolicy.builder()
            .name(config.getPolicyName())
            .incidentPreference(config.getIncidentPreference())
            .build();

        Optional<AlertsPolicy> policyOptional = api.getAlertsPoliciesApi().getByName(config.getPolicyName());

        if (policyOptional.isPresent()) {
            AlertsPolicy oldPolicy = policyOptional.get();
            if (!StringUtils.equals(configAlertPolicy.getIncidentPreference(), oldPolicy.getIncidentPreference())) {
                api.getAlertsPoliciesApi().delete(oldPolicy.getId());
                api.getAlertsPoliciesApi().create(configAlertPolicy);
            }
        } else {
            api.getAlertsPoliciesApi().create(configAlertPolicy);
        }
    }
}
