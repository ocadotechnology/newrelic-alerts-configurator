package com.ocadotechnology.newrelic.apiclient.model.dashboards;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.apiclient.model.dashboards.dashboard.DashboardMetadata;
import com.ocadotechnology.newrelic.apiclient.model.dashboards.widget.Widget;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * See <a href="https://docs.newrelic.com/docs/insights/insights-api/manage-dashboards/insights-dashboard-api#schema">Doc</a>
 */
@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class Dashboard {
    @JsonProperty
    Integer id;
    @JsonProperty
    String title;
    @JsonProperty
    String description;
    @JsonProperty
    String icon;
    @JsonProperty
    String visibility;
    @JsonProperty
    String editable;
    @JsonProperty("ui_url")
    String uiUrl;
    @JsonProperty("api_url")
    String apiUrl;
    @JsonProperty("owner_email")
    String ownerEmail;
    @JsonProperty
    DashboardMetadata metadata;
    @JsonProperty
    List<Widget> widgets;
}
