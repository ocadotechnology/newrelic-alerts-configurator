package com.ocadotechnology.newrelic.apiclient.model.dashboards.widget;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * See <a href="https://docs.newrelic.com/docs/insights/insights-api/manage-dashboards/insights-dashboard-api#widget-data">Doc</a>
 */
@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class Widget {
    @JsonProperty("widget_id")
    Integer widgetId;
    @JsonProperty("account_id")
    Integer accountId;
    @JsonProperty
    String visualization;
    @JsonProperty
    WidgetLayout layout;
    @JsonProperty
    List<WidgetData> data;
    @JsonProperty
    WidgetPresentation presentation;
}
