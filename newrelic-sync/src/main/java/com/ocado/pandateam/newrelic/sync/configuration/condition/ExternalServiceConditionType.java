package com.ocado.pandateam.newrelic.sync.configuration.condition;

import lombok.Getter;

@Getter
public enum ExternalServiceConditionType {
    APM("apm_external_service");

    String typeString;

    ExternalServiceConditionType(String typeString) {
        this.typeString = typeString;
    }
}
