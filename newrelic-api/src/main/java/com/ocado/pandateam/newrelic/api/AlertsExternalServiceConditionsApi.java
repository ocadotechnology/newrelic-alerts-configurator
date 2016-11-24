package com.ocado.pandateam.newrelic.api;

import com.ocado.pandateam.newrelic.api.model.conditions.external.AlertsExternalServiceCondition;

import java.util.List;

public interface AlertsExternalServiceConditionsApi {
    /**
     * Lists Alerts Conditions for external services for the given policy.
     *
     * @param policyId id of the policy containing alerts conditions
     * @return list of all existing {@link AlertsExternalServiceCondition} from the given policy
     */
    List<AlertsExternalServiceCondition> list(int policyId);

    /**
     * Creates Alerts Condition for external service instance within specified policy.
     *
     * @param policyId  id of the policy to be updated
     * @param condition condition definition to be created
     * @return created {@link AlertsExternalServiceCondition}
     */
    AlertsExternalServiceCondition create(int policyId, AlertsExternalServiceCondition condition)
    ;

    /**
     * Updates Alerts Condition for external service definition.
     *
     * @param conditionId id of the condition to be updated
     * @param condition   condition definition to be updated
     * @return created {@link AlertsExternalServiceCondition}
     */
    AlertsExternalServiceCondition update(int conditionId, AlertsExternalServiceCondition condition)
    ;

    /**
     * Deletes Alerts Condition for external service.
     *
     * @param conditionId id of the condition to be updated
     * @return deleted {@link AlertsExternalServiceCondition}
     */
    AlertsExternalServiceCondition delete(int conditionId);
}
