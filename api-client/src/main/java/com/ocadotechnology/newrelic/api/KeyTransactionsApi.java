package com.ocadotechnology.newrelic.api;

import com.ocadotechnology.newrelic.api.model.transactions.KeyTransaction;

import java.util.Optional;

public interface KeyTransactionsApi {
    /**
     * Gets {@link KeyTransaction} object using its name.
     *
     * @param keyTransactionName name of the key transaction registered in NewRelic
     * @return Optional containing {@link KeyTransaction} object, or empty if transaction not found
     */
    Optional<KeyTransaction> getByName(String keyTransactionName);

    /**
     * Gets {@link KeyTransaction} object using its name.
     *
     * @param keyTransactionId id of the key transaction registered in NewRelic
     * @return found {@link KeyTransaction}
     */
    KeyTransaction getById(int keyTransactionId);
}
