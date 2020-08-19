package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.DashboardConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.exception.NewRelicSyncException;
import com.ocadotechnology.newrelic.apiclient.DashboardsApi;
import com.ocadotechnology.newrelic.apiclient.NewRelicApi;
import com.ocadotechnology.newrelic.apiclient.model.dashboards.Dashboard;
import com.ocadotechnology.newrelic.apiclient.model.dashboards.Dashboard.DashboardBuilder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.lang.String.format;

@Slf4j
class DashboardConfigurator {

    private final DashboardsApi api;

    DashboardConfigurator(@NonNull NewRelicApi api) {
        this.api = api.getDashboardsApi();
    }

    void sync(@NonNull DashboardConfiguration config) {
        LOG.info("Synchronizing dashboard {}...", config.getTitle());
        List<Dashboard> matchingDashboards = api.getByTitle(config.getTitle());
        if (matchingDashboards.size() > 1) {
            throw new NewRelicSyncException(format("There are more than one dashboards matching the title %s", config.getTitle()));
        }

        if (matchingDashboards.isEmpty()) {
            throw new NewRelicSyncException(format("Unable to find dashboard with title '%s' for update", config.getTitle()));
        }

        Dashboard dashboard = getDashboardForUpdate(config, matchingDashboards.get(0));
        api.update(dashboard);

        LOG.info("Dashboard {} synchronized", config.getTitle());
    }

    private Dashboard getDashboardForUpdate(DashboardConfiguration config, Dashboard dashboard) {
        DashboardBuilder builder = api.getById(dashboard.getId()).toBuilder();

        if (config.getTitle() != null) {
            builder.title(config.getTitle());
        }

        if (config.getDescription() != null) {
            builder.description(config.getDescription());
        }
        if (config.getIcon() != null) {
            builder.icon(config.getIcon());
        }
        if (config.getVisibility() != null) {
            builder.visibility(config.getVisibility());
        }
        if (config.getEditable() != null) {
            builder.editable(config.getEditable());
        }
        if (config.getOwnerEmail() != null) {
            builder.ownerEmail(config.getOwnerEmail());
        }
        if (config.getWidgets() != null) {
            builder.widgets(config.getWidgets());
        }
        return builder.build();
    }
}
