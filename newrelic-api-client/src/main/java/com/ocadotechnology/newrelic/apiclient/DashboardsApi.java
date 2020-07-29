package com.ocadotechnology.newrelic.apiclient;

import com.ocadotechnology.newrelic.apiclient.model.dashboards.Dashboard;

import java.util.List;

/**
 * See <a href="https://docs.newrelic.com/docs/insights/insights-api/manage-dashboards/insights-dashboard-api">Doc</a>
 */
public interface DashboardsApi {

    /**
     * View a list of {@link Dashboard}. The list shows dashboard metadata only; no widgets will appear in the list.
     *
     * @param dashboardTitle name of the dashboard(s) registered in NewRelic. This may be a partial name.
     * @return List containing {@link Dashboard} objects, or empty if no dashboard including dashboardTitle exist.
     * This
     */
    List<Dashboard> getByTitle(String dashboardTitle);

    /**
     * View an existing {@link Dashboard} and all accessible widgets for the dashboard id.
     *
     * @param dashboardId id of the dashboard registered in NewRelic
     * @return found {@link Dashboard}
     */
    Dashboard getById(int dashboardId);

    /**
     * Create a new {@link Dashboard}.
     * <p>
     * The API permits a maximum of 300 widgets when creating or updating a dashboard. Attempting to POST more than 300 widgets will produce an error.
     *
     * @param dashboard to be created in NewRelic
     * @return created {@link Dashboard}
     */
    Dashboard create(Dashboard dashboard);

    /**
     * Update an existing {@link Dashboard} for the dashboard id.
     * <p>
     * The API permits a maximum of 300 widgets when creating or updating a dashboard. Attempting to PUT more than 300 widgets will produce an error.
     *
     * @param dashboard to be updated in NewRelic
     * @return updated {@link Dashboard}
     */
    Dashboard update(Dashboard dashboard);

    /**
     * Delete an existing {@link Dashboard} indicated by the dashboard id.
     *
     * @param dashboardId of the dashboard to be deleted
     * @return deleted {@link Dashboard}
     */
    Dashboard delete(int dashboardId);

}
