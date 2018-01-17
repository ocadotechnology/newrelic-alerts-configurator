package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import lombok.NonNull;

interface PolicyItemConfigurator {
    void sync(@NonNull PolicyConfiguration config);
}
