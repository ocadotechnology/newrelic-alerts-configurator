package com.ocado.pandateam.newrelic.api;

import com.ocado.pandateam.newrelic.api.internal.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.conditions.external.AlertsExternalServiceCondition;

import java.util.List;

public interface AlertsExternalServiceConditionsApi {
    /**
     * Lists Alerts Conditions for external services for the given policy.
     *
     * @param policyId id of the policy containing alerts conditions
     * @return list of all existing {@link AlertsExternalServiceCondition} from the given policy
     * @throws NewRelicApiException when received error response
     */
    List<AlertsExternalServiceCondition> list(int policyId) throws NewRelicApiException;

    /**
     * Creates Alerts Condition for external service instance within specified policy.
     *
     * @param policyId  id of the policy to be updated
     * @param condition condition definition to be created
     * @return created {@link AlertsExternalServiceCondition}
     * @throws NewRelicApiException when received error response
     */
    AlertsExternalServiceCondition create(int policyId, AlertsExternalServiceCondition condition)
            throws NewRelicApiException;

    /**
     * Updates Alerts Condition for external service definition.
     *
     * @param conditionId id of the condition to be updated
     * @param condition   condition definition to be updated
     * @return created {@link AlertsExternalServiceCondition}
     * @throws NewRelicApiException when received error response
     */
    AlertsExternalServiceCondition update(int conditionId, AlertsExternalServiceCondition condition)
            throws NewRelicApiException;

    /**
     * Deletes Alerts Condition for external service.
     *
     * @param conditionId id of the condition to be updated
     * @return deleted {@link AlertsExternalServiceCondition}
     * @throws NewRelicApiException when received error response
     */
    AlertsExternalServiceCondition delete(int conditionId) throws NewRelicApiException;
}
