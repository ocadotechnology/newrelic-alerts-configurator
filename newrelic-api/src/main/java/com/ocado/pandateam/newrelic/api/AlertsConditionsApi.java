package com.ocado.pandateam.newrelic.api;

import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.conditions.AlertsCondition;

import java.util.List;

public interface AlertsConditionsApi {
    /**
     * Lists Alerts Conditions for the given policy.
     *
     * @param policyId - id of the policy containing alerts conditions
     * @return list of all existing {@link AlertsCondition} from the given policy
     * @throws NewRelicApiException when received error response
     */
    List<AlertsCondition> list(int policyId) throws NewRelicApiException;

    /**
     * Creates Alerts Condition instance within specified policy.
     *
     * @param policyId  - id of the policy to be updated
     * @param condition - condition definition to be created
     * @return created {@link AlertsCondition}
     * @throws NewRelicApiException when received error response
     */
    AlertsCondition create(int policyId, AlertsCondition condition) throws NewRelicApiException;

    /**
     * Updates Alerts Condition definition.
     *
     * @param conditionId - id of the condition to be updated
     * @param condition   - condition definition to be updated
     * @return created {@link AlertsCondition}
     * @throws NewRelicApiException when received error response
     */
    AlertsCondition update(int conditionId, AlertsCondition condition) throws NewRelicApiException;

    /**
     * Deletes Alerts Condition.
     *
     * @param conditionId - id of the condition to be updated
     * @return deleted {@link AlertsCondition}
     * @throws NewRelicApiException when received error response
     */
    AlertsCondition delete(int conditionId) throws NewRelicApiException;
}
