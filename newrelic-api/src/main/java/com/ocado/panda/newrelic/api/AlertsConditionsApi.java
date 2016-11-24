package com.ocado.panda.newrelic.api;

import com.ocado.panda.newrelic.api.model.conditions.AlertsCondition;

import java.util.List;

public interface AlertsConditionsApi {
    /**
     * Lists Alerts Conditions for the given policy.
     *
     * @param policyId id of the policy containing alerts conditions
     * @return list of all existing {@link AlertsCondition} from the given policy
     */
    List<AlertsCondition> list(int policyId);

    /**
     * Creates Alerts Condition instance within specified policy.
     *
     * @param policyId  id of the policy to be updated
     * @param condition condition definition to be created
     * @return created {@link AlertsCondition}
     */
    AlertsCondition create(int policyId, AlertsCondition condition);

    /**
     * Updates Alerts Condition definition.
     *
     * @param conditionId id of the condition to be updated
     * @param condition   condition definition to be updated
     * @return created {@link AlertsCondition}
     */
    AlertsCondition update(int conditionId, AlertsCondition condition);

    /**
     * Deletes Alerts Condition.
     *
     * @param conditionId id of the condition to be updated
     * @return deleted {@link AlertsCondition}
     */
    AlertsCondition delete(int conditionId);
}
