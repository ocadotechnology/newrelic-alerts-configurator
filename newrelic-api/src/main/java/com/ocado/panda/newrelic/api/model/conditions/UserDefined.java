package com.ocado.panda.newrelic.api.model.conditions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class UserDefined {
    @JsonProperty
    String metric;
    @JsonProperty("value_function")
    String valueFunction;
}
