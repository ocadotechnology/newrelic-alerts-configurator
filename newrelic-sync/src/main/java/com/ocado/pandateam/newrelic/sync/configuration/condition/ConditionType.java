package com.ocado.pandateam.newrelic.sync.configuration.condition;

public enum ConditionType {
    APM_APP("apm_app_metric");

    String typeString;

    ConditionType(String typeString) {
        this.typeString = typeString;
    }

    public String getTypeString() {
        return typeString;
    }
}
