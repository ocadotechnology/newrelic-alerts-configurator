package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConditionType {
    APM_APP("apm_app_metric"),
    APM_KEY_TRANSACTION("apm_kt_metric"),
    SERVERS_METRIC("servers_metric"),
    APM_JVM("apm_jvm_metric"),
    BROWSER_METRIC("browser_metric");

    private final String typeString;
}
