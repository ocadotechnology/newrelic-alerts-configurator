package com.ocadotechnology.newrelic.alertsconfigurator;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.exception.NewRelicSyncException;
import com.ocadotechnology.newrelic.apiclient.NewRelicApi;
import com.ocadotechnology.newrelic.apiclient.PolicyItemApi;
import com.ocadotechnology.newrelic.apiclient.model.PolicyItem;
import com.ocadotechnology.newrelic.apiclient.model.policies.AlertsPolicy;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
abstract class AbstractPolicyItemConfigurator<T extends PolicyItem, U> implements PolicyItemConfigurator {

    protected final NewRelicApi api;

    AbstractPolicyItemConfigurator(@NonNull NewRelicApi api) {
        this.api = api;
    }

    @Override
    public void sync(@NonNull PolicyConfiguration config) {
        if (! getConfigItems(config).isPresent()) {
            LOG.info("No items for policy {} - skipping...", config.getPolicyName());
            return;
        }

        LOG.info("Synchronizing items for policy {}...", config.getPolicyName());

        AlertsPolicy policy = api.getAlertsPoliciesApi().getByName(config.getPolicyName()).orElseThrow(
                () -> new NewRelicSyncException(format("Policy %s does not exist", config.getPolicyName())));

        List<T> allItems = getItemsApi().list(policy.getId());
        List<Integer> updatedItemsIds = createOrUpdateAlertsNrqlConditions(policy, getConfigItems(config).get(), allItems);

        cleanupOldItems(policy, allItems, updatedItemsIds);
        LOG.info("Items for policy {} synchronized", config.getPolicyName());
    }

    private List<Integer> createOrUpdateAlertsNrqlConditions(AlertsPolicy policy,
                                                             Collection<U> itemsFromConfig,
                                                             Collection<T> allItems) {
        List<T> updatedItems = new LinkedList<>();
        for (U itemFromConfig : itemsFromConfig) {
            T convertedItemFromConfig = convertFromConfigItem(itemFromConfig);
            Optional<T> itemToUpdate = findItemsToUpdate(allItems, convertedItemFromConfig);

            if (itemToUpdate.isPresent()) {
                T updatedItem = updateItem(policy, convertedItemFromConfig, itemToUpdate.get());
                updatedItems.add(updatedItem);
            } else {
                createAlertsNrqlCondition(policy, convertedItemFromConfig);
            }
        }

        return updatedItems.stream()
                .map(PolicyItem::getId)
                .collect(Collectors.toList());
    }

    private void createAlertsNrqlCondition(AlertsPolicy policy, T itemFromConfig) {
        T newItem = getItemsApi().create(policy.getId(), itemFromConfig);
        LOG.info("Item {} (id: {}) created for policy {} (id: {})", newItem.getName(), newItem.getId(), policy.getName(), policy.getId());
    }

    private T updateItem(AlertsPolicy policy, T itemFromConfig, T itemToUpdate) {
        T updatedItem = getItemsApi().update(itemToUpdate.getId(), itemFromConfig);
        LOG.info("Item {} (id: {}) updated for policy {} (id: {})", updatedItem.getName(), updatedItem.getId(), policy.getName(), policy.getId());
        return updatedItem;
    }

    private void cleanupOldItems(AlertsPolicy policy, List<T> allItems,
                                 Collection<Integer> updatedItemsIds) {
        allItems.stream()
                .filter(item -> !updatedItemsIds.contains(item.getId()))
                .forEach(
                        item -> {
                            getItemsApi().delete(policy.getId(), item.getId());
                            LOG.info("Item {} (id: {}) removed from policy {} (id: {})", item.getName(), item.getId(), policy.getName(), policy.getId());
                        }
                );
    }

    private Optional<T> findItemsToUpdate(Collection<T> allItems, T itemFromConfig) {
        return allItems.stream()
                .filter(item -> sameInstance(item, itemFromConfig))
                .findAny();
    }

    protected abstract Optional<Collection<U>> getConfigItems(PolicyConfiguration config);

    protected abstract T convertFromConfigItem(U configItem);

    protected abstract boolean sameInstance(T t1, T t2);

    protected abstract PolicyItemApi<T> getItemsApi();
}