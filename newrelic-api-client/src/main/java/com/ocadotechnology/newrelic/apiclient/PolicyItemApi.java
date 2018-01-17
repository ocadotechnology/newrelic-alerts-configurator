package com.ocadotechnology.newrelic.apiclient;

import java.util.List;

public interface PolicyItemApi<T> {
    /**
     * Lists items for the given policy.
     *
     * @param policyId id of the policy containing items
     * @return list of all existing {@link T} from the given policy
     */
    List<T> list(int policyId);

    /**
     * Creates item instance within specified policy.
     *
     * @param policyId  id of the policy to be updated
     * @param item item definition to be created
     * @return created {@link T}
     */
    T create(int policyId, T item);

    /**
     * Updates item definition.
     *
     * @param id id of the item to be updated
     * @param item   item definition to be updated
     * @return created {@link T}
     */
    T update(int id, T item);

    /**
     * Deletes item.
     *
     * @param id id of the item to be deleted
     * @return deleted {@link T}
     */
    T delete(int id);
}
