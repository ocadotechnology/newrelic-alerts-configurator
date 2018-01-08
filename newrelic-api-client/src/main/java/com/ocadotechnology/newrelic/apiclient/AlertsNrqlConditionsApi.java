package com.ocadotechnology.newrelic.apiclient;

import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.AlertsNrqlCondition;

import java.util.List;

public interface AlertsNrqlConditionsApi {
    /**
     * Lists Alerts NRQL Conditions for the given policy.
     *
     * @param policyId id of the policy containing alerts NRQL conditions
     * @return list of all existing {@link AlertsNrqlCondition} from the given policy
     */
    List<AlertsNrqlCondition> list(int policyId);

    /**
     * Creates Alerts NRQL Condition instance within specified policy.
     *
     * @param policyId  id of the policy to be updated
     * @param condition condition definition to be created
     * @return created {@link AlertsNrqlCondition}
     */
    AlertsNrqlCondition create(int policyId, AlertsNrqlCondition condition);

    /**
     * Updates Alerts NRQL Condition definition.
     *
     * @param conditionId id of the condition to be updated
     * @param condition   condition definition to be updated
     * @return created {@link AlertsNrqlCondition}
     */
    AlertsNrqlCondition update(int conditionId, AlertsNrqlCondition condition);

    /**
     * Deletes Alerts NRQL Condition.
     *
     * @param conditionId id of the condition to be updated
     * @return deleted {@link AlertsNrqlCondition}
     */
    AlertsNrqlCondition delete(int conditionId);
}
