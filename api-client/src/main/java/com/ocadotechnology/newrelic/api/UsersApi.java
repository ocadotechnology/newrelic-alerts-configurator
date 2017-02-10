package com.ocadotechnology.newrelic.api;

import com.ocadotechnology.newrelic.api.model.users.User;

import java.util.Optional;

public interface UsersApi {
    /**
     * Gets {@link User} object using its email.
     *
     * @param userEmail email of the NewRelic user
     * @return Optional containing {@link User} object, or empty if user not found
     */
    Optional<User> getByEmail(String userEmail);

    /**
     * Gets {@link User} object using its id.
     *
     * @param userId id of the NewRelic user
     * @return found {@link User}
     */
    User getById(int userId);
}
