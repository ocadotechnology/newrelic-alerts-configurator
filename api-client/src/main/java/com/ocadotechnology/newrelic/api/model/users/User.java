package com.ocadotechnology.newrelic.api.model.users;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * See <a href="https://rpm.newrelic.com/api/explore/users/list">Doc</a>
 */
@Value
@Builder
@AllArgsConstructor
public class User {
    @JsonProperty
    Integer id;
    @JsonProperty("first_name")
    String firstName;
    @JsonProperty("last_name")
    String lastName;
    @JsonProperty
    String email;
    @JsonProperty
    String role;
}
