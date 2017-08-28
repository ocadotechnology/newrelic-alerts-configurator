package com.ocadotechnology.newrelic.apiclient.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.apiclient.model.users.User;
import lombok.Value;

@Value
public class UserWrapper {
    @JsonProperty
    User user;
}
