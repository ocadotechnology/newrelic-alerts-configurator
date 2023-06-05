package com.ocadotechnology.newrelic.graphql.apiclient.internal.transport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ocadotechnology.newrelic.apiclient.model.policies.AlertsPolicy;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.mutation.EmptyMutationResult;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.mutation.MutationResult;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.mutation.NotificationChannelResult;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.mutation.NotificationChannelsResult;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.util.*;


@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@AllArgsConstructor
public class Result {
    private final Data data;

    private final List<Error> errors;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Value
    static class Data {
        Actor actor;
        AlertsPolicyCreate alertsPolicyCreate;
        EmptyMutationResult alertsNotificationChannelDelete;
        NotificationChannelsResult alertsNotificationChannelsAddToPolicy;
        NotificationChannelsResult alertsNotificationChannelsRemoveFromPolicy;

        NotificationChannelResult alertsNotificationChannelCreate;

        NrqlCondition alertsNrqlConditionStaticCreate;
        NrqlCondition alertsNrqlConditionStaticUpdate;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Value
    static class Actor {
        EntitySearch entitySearch;
        Account account;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Value
    static class AlertsPolicyCreate {
        String id;
        String name;
        String incidentPreference;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Value
    static class Account {
        Alerts alerts;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Value
    static class Alerts {
        AlertsPolicy policy;
        NotificationChannel notificationChannel;
        NotificationChannels notificationChannels;
        PoliciesSearch policiesSearch;
        NrqlConditionsSearch nrqlConditionsSearch;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Value
    static class PoliciesSearch {
        List<AlertsPolicy> policies;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Value
    static class EntitySearch {
        Results results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Value
    static class Results {
        List<Map<Object, Object>> entities;
    }

    public boolean hasErrors() {
        return Objects.nonNull(errors);
    }

    public boolean notFound() {
        return Optional.ofNullable(errors)
                .orElseGet(ArrayList::new).stream()
                .anyMatch(error -> "Not Found".equalsIgnoreCase(error.getMessage()));
    }

    public List<Error> getErrors() {
        return Optional.ofNullable(errors).orElseGet(ArrayList::new);
    }

    public List<AlertsPolicy> getPolicies() {
        return getAlerts()
                .map(alerts -> alerts.policiesSearch)
                .map(policiesSearch -> policiesSearch.policies).orElseGet(ArrayList::new);
    }

    public Optional<AlertsPolicy> getAlertsPolicyCreate() {
        return getData()
                .map(data -> data.alertsPolicyCreate)
                .map(alertsPolicyCreate -> AlertsPolicy.builder()
                        .id(Optional.ofNullable(alertsPolicyCreate.id).map(Integer::parseInt).orElse(null))
                        .name(alertsPolicyCreate.name)
                        .incidentPreference(alertsPolicyCreate.incidentPreference)
                        .build());
    }

    public Optional<MutationResult<List<Integer>>> getAlertsNotificationChannelsAddToPolicy() {
        return getData().map(data -> data.alertsNotificationChannelsAddToPolicy);
    }

    public Optional<MutationResult<List<Integer>>> getAlertsNotificationChannelsRemoveFromPolicy() {
        return getData().map(data -> data.alertsNotificationChannelsRemoveFromPolicy);
    }

    public Optional<NotificationChannelResult> getAlertsNotificationChannelCreate() {
        return getData().map(data -> data.alertsNotificationChannelCreate);
    }

    public Optional<MutationResult<Void>> getAlertsNotificationChannelDelete() {
        return getData().map(data -> data.alertsNotificationChannelDelete);
    }

    public Optional<AlertsPolicy> getAlertsPolicy() {
        return getAlerts().map(alerts -> alerts.policy);
    }

    public Optional<NotificationChannel> getNotificationChannel() {
        return getAlerts().map(alerts -> alerts.notificationChannel);
    }

    public Optional<NotificationChannels> getNotificationChannels() {
        return getAlerts().map(alerts -> alerts.notificationChannels);
    }

    public Optional<NrqlConditionsSearch> getNrqlConditionsSearch() {
        return getAlerts().map(alerts -> alerts.nrqlConditionsSearch);
    }

    public Optional<NrqlCondition> getAlertsNrqlConditionStaticCreate() {
        return getData().map(data -> data.alertsNrqlConditionStaticCreate);
    }

    public Optional<NrqlCondition> getAlertsNrqlConditionStaticUpdate() {
        return getData().map(data -> data.alertsNrqlConditionStaticUpdate);
    }

    private Optional<Data> getData() {
        return Optional.ofNullable(data);
    }

    private Optional<Alerts> getAlerts() {
        return getData()
                .map(data -> data.actor)
                .map(actor -> actor.account)
                .map(account -> account.alerts);
    }

}
