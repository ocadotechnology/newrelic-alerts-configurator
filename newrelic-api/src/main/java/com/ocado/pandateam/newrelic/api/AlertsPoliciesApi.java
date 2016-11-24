package com.ocado.pandateam.newrelic.api;

import com.ocado.pandateam.newrelic.api.internal.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicy;
import com.ocado.pandateam.newrelic.api.model.policies.AlertsPolicyChannels;

import java.util.Optional;

public interface AlertsPoliciesApi {
    /**
     * Gets {@link AlertsPolicy} object using its name.
     *
     * @param alertsPolicyName name of the alert policy registered in NewRelic
     * @return Optional containing {@link AlertsPolicy} object, or empty if alert policy not found
     * @throws NewRelicApiException when more than one response returned or received error response
     */
    Optional<AlertsPolicy> getByName(String alertsPolicyName) throws NewRelicApiException;

    /**
     * Creates Alerts Policy instance.
     *
     * @param policy policy definition to be created
     * @return created {@link AlertsPolicy}
     * @throws NewRelicApiException when received error response
     */
    AlertsPolicy create(AlertsPolicy policy) throws NewRelicApiException;

    /**
     * Deletes Alerts Policy instance.
     *
     * @param policyId id of the policy to be removed
     * @return deleted {@link AlertsPolicy}
     * @throws NewRelicApiException when received error response
     */
    AlertsPolicy delete(int policyId) throws NewRelicApiException;

    /**
     * Associates given channels to the policy. This method does not remove previously linked channel but not provided in the list.
     *
     * @param channels {@link AlertsPolicyChannels} object mapping policy id to the list of channels ids
     * @return {@link AlertsPolicyChannels} when successfully set
     * @throws NewRelicApiException when received error response
     */
    AlertsPolicyChannels updateChannels(AlertsPolicyChannels channels) throws NewRelicApiException;
}
