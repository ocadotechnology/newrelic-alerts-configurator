package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.apiclient.NewRelicApi;
import com.ocadotechnology.newrelic.apiclient.model.policies.AlertsPolicy;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static java.lang.String.format;

@Slf4j
class PolicyConfigurator {
    private final NewRelicApi api;

    PolicyConfigurator(@NonNull NewRelicApi api) {
        this.api = api;
    }

    void sync(@NonNull PolicyConfiguration config) {
        LOG.info("Synchronizing policy {}...", config.getPolicyName());

        AlertsPolicy alertsPolicyFromConfig = AlertsPolicy.builder()
            .name(config.getPolicyName())
            .incidentPreference(config.getIncidentPreference().name())
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
