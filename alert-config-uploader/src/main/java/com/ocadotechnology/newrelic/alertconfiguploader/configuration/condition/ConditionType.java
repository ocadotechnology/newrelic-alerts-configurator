package com.ocadotechnology.newrelic.alertconfiguploader.configuration.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConditionType {
    APM_APP("apm_app_metric"),
    APM_KEY_TRANSACTION("apm_kt_metric"),
    SERVERS_METRIC("servers_metric");

    private final String typeString;
}
