package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.sync.configuration.PolicyConfiguration;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.logging.Logger;

import static java.lang.String.format;

class PolicySynchronizer {
    private static final Logger LOG = Logger.getLogger(PolicySynchronizer.class.getName());

    private final NewRelicApi api;
    private final PolicyConfiguration config;

    PolicySynchronizer(@NonNull NewRelicApi api, @NonNull PolicyConfiguration config) {
        this.config = config;
        this.api = api;
    }

    void sync() {
        LOG.info(format("Synchronizing policy %s...", config.getPolicyName()));

        AlertsPolicy alertsPolicyFromConfig = AlertsPolicy.builder()
            .name(config.getPolicyName())
            .incidentPreference(config.getIncidentPreference())
            .build();

        Optional<AlertsPolicy> policyOptional = api.getAlertsPoliciesApi().getByName(config.getPolicyName());

        if (policyOptional.isPresent()) {
            AlertsPolicy oldPolicy = policyOptional.get();
            if (!StringUtils.equals(alertsPolicyFromConfig.getIncidentPreference(), oldPolicy.getIncidentPreference())) {
                api.getAlertsPoliciesApi().delete(oldPolicy.getId());
                api.getAlertsPoliciesApi().create(alertsPolicyFromConfig);
                LOG.info(format("Policy %s updated!", config.getPolicyName()));
            }
        } else {
            api.getAlertsPoliciesApi().create(alertsPolicyFromConfig);
            LOG.info(format("Policy %s created!", config.getPolicyName()));
        }
        LOG.info(format("Policy %s synchronized!", config.getPolicyName()));
    }
}
