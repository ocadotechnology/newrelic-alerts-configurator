package com.ocadotechnology.newrelic.apiclient.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.apiclient.model.users.User;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class UserList extends ObjectList<User, UserList> {
    @JsonCreator
    public UserList(@JsonProperty("users") List<User> items) {
        super(items, UserList::new);
    }
}
