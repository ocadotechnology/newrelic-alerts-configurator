package com.ocado.pandateam.newrelic.api;

import com.ocado.pandateam.newrelic.api.model.transactions.KeyTransaction;

import java.util.Optional;

public interface KeyTransactionsApi {
    /**
     * Gets {@link KeyTransaction} object using its name.
     *
     * @param keyTransactionName name of the key transaction registered in NewRelic
     * @return Optional containing {@link KeyTransaction} object, or empty if transaction not found
     * @throws NewRelicApiException when more than one response returned or received error response
     */
    Optional<KeyTransaction> getByName(String keyTransactionName);

    /**
     * Gets {@link KeyTransaction} object using its name.
     *
     * @param keyTransactionId id of the key transaction registered in NewRelic
     * @return found {@link KeyTransaction}
     * @throws NewRelicApiException when key transaction with given id not found
     */
    KeyTransaction getById(int keyTransactionId);
}
