package com.ocadotechnology.newrelic.apiclient.internal;


import com.ocadotechnology.newrelic.apiclient.DashboardsApi;
import com.ocadotechnology.newrelic.apiclient.internal.client.NewRelicClient;
import com.ocadotechnology.newrelic.apiclient.internal.model.DashboardList;
import com.ocadotechnology.newrelic.apiclient.internal.model.DashboardWrapper;
import com.ocadotechnology.newrelic.apiclient.model.dashboards.Dashboard;
import org.glassfish.jersey.uri.UriComponent;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.glassfish.jersey.uri.UriComponent.Type.QUERY_PARAM_SPACE_ENCODED;

public class DefaultDashboardsApi extends ApiBase implements DashboardsApi {

    private static final String DASHBOARDS_URL = "/v2/dashboards.json";

    private static final String DASHBOARD_URL = "/v2/dashboards/{dashboard_id}.json";

    public DefaultDashboardsApi(NewRelicClient client) {
        super(client);
    }

    @Override
    public Dashboard getById(int dashboardId) {
        return client
                .target(DASHBOARD_URL)
                .resolveTemplate("dashboard_id", dashboardId)
                .request(APPLICATION_JSON_TYPE)
                .get(DashboardWrapper.class)
                .getDashboard();
    }

    @Override
    public List<Dashboard> getByTitle(String dashboardTitle) {
        String dashboardTitleEncoded = UriComponent.encode(dashboardTitle, QUERY_PARAM_SPACE_ENCODED);
        Invocation.Builder builder = client
                .target(DASHBOARDS_URL)
                .queryParam("filter[title]", dashboardTitleEncoded)
                .request(APPLICATION_JSON_TYPE);
        return getPageable(builder, DashboardList.class)
                .getList();
    }

    @Override
    public Dashboard create(Dashboard dashboard) {
        return client
                .target(DASHBOARDS_URL)
                .request(APPLICATION_JSON_TYPE)
                .post(Entity.entity(new DashboardWrapper(dashboard), APPLICATION_JSON_TYPE), DashboardWrapper.class)
                .getDashboard();
    }

    @Override
    public Dashboard update(Dashboard dashboard) {
        return client
                .target(DASHBOARD_URL)
                .resolveTemplate("dashboard_id", dashboard.getId())
                .request(APPLICATION_JSON_TYPE)
                .put(Entity.entity(new DashboardWrapper(dashboard), APPLICATION_JSON_TYPE), DashboardWrapper.class)
                .getDashboard();
    }

    @Override
    public Dashboard delete(int dashboardId) {
        return client
                .target(DASHBOARD_URL)
                .resolveTemplate("dashboard_id", dashboardId)
                .request(APPLICATION_JSON_TYPE)
                .delete(DashboardWrapper.class)
                .getDashboard();
    }
}
