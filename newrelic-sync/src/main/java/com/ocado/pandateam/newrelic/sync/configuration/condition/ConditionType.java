package com.ocado.pandateam.newrelic.sync.configuration.condition;

public enum ConditionType {
    APM_APP("apm_app_metric");

    String type;

    ConditionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
