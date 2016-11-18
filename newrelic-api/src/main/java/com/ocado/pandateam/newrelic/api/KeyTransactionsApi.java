package com.ocado.pandateam.newrelic.api;

import com.ocado.pandateam.newrelic.api.model.transactions.KeyTransaction;

import java.util.Optional;

public interface KeyTransactionsApi {

    Optional<KeyTransaction> getByName(String name);

    KeyTransaction getById(int keyTransactionId);
}
