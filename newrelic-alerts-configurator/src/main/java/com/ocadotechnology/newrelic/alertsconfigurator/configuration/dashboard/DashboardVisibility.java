package com.ocadotechnology.newrelic.alertsconfigurator.configuration.dashboard;

import lombok.Getter;

@Getter
public enum DashboardVisibility {
    OWNER("owner"),
    ALL("all");

    private final String visibility;

    DashboardVisibility(String visibility) {
        this.visibility = visibility;
    }
}
