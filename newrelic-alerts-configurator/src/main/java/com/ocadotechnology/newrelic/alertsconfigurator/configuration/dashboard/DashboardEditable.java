package com.ocadotechnology.newrelic.alertsconfigurator.configuration.dashboard;

import lombok.Getter;

@Getter
public enum DashboardEditable {
    READ_ONLY("read_only"),
    EDITABLE_BY_OWNER("editable_by_owner"),
    EDITABLE_BY_ALL("editable_by_all");

    private final String editable;

    DashboardEditable(String editable) {
        this.editable = editable;
    }
}
