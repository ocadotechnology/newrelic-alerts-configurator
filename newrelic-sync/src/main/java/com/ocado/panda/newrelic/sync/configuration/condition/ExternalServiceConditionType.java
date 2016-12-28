package com.ocado.panda.newrelic.sync.configuration.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExternalServiceConditionType {
    APM("apm_external_service");

    private final String typeString;
}
