package com.ocadotechnology.newrelic.alertsconfigurator.configuration;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.dashboard.DashboardEditable;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.dashboard.DashboardIcon;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.dashboard.DashboardVisibility;
import com.ocadotechnology.newrelic.apiclient.model.dashboards.widget.Widget;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class DashboardConfiguration {

    @NonNull
    private final String title;

    private final String description;

    private final DashboardIcon icon;

    private final DashboardVisibility visibility;

    private final DashboardEditable editable;

    private final String ownerEmail;

    private final List<Widget> widgets;

    public String getIcon() {
        return icon == null ? null : icon.getIcon();
    }

    public String getVisibility() {
        return visibility == null ? null : visibility.getVisibility();
    }

    public String getEditable() {
        return editable == null ? null : editable.getEditable();
    }
}
