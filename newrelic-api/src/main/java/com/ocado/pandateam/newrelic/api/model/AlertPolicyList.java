package com.ocado.pandateam.newrelic.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class AlertPolicyList implements ObjectList<AlertPolicy> {
    @JsonProperty("policies")
    List<AlertPolicy> list;
}
