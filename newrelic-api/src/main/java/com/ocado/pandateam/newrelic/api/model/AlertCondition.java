package com.ocado.pandateam.newrelic.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class AlertCondition {
    Integer id;
    String type;
    String name;
    boolean enabled;
    Integer[] entities;
    String metric;
    @JsonProperty("runbook_url")
    String runbookUrl;
    Terms[] terms;
    UserDefined userDefined;
}
