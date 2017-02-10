package com.ocadotechnology.newrelic.alertconfiguploader;

import com.ocadotechnology.newrelic.alertconfiguploader.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.api.NewRelicApi;
import com.ocadotechnology.newrelic.api.model.policies.AlertsPolicy;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static java.lang.String.format;

@Slf4j
class PolicySynchronizer {
    private final NewRelicApi api;

    PolicySynchronizer(@NonNull NewRelicApi api) {
        this.api = api;
    }

    void sync(@NonNull PolicyConfiguration config) {
        LOG.info("Synchronizing policy {}...", config.getPolicyName());

        AlertsPolicy alertsPolicyFromConfig = AlertsPolicy.builder()
            .name(config.getPolicyName())
            .incidentPreference(config.getIncidentPreference())
            .build();

        Optional<AlertsPolicy> policy = api.getAlertsPoliciesApi().getByName(config.getPolicyName());

        if (policy.isPresent()) {
            AlertsPolicy oldPolicy = policy.get();
            if (!StringUtils.equals(alertsPolicyFromConfig.getIncidentPreference(), oldPolicy.getIncidentPreference())) {
                api.getAlertsPoliciesApi().delete(oldPolicy.getId());
                api.getAlertsPoliciesApi().create(alertsPolicyFromConfig);
                LOG.info(format("Policy %s updated", config.getPolicyName()));
            }
        } else {
            api.getAlertsPoliciesApi().create(alertsPolicyFromConfig);
            LOG.info("Policy {} created", config.getPolicyName());
        }
        LOG.info("Policy {} synchronized", config.getPolicyName());
    }
}
