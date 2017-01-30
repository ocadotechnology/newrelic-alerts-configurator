package com.ocado.panda.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.panda.newrelic.api.model.ObjectList;
import com.ocado.panda.newrelic.api.model.users.User;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class UserList extends ObjectList<User, UserList> {
    @JsonCreator
    public UserList(@JsonProperty("users") List<User> items) {
        super(items, UserList::new);
    }
}
