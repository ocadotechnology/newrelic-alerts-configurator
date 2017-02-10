package com.ocadotechnology.newrelic.alertconfiguploader.internal.entities;


import com.ocadotechnology.newrelic.alertconfiguploader.exception.NewRelicSyncException;
import com.ocadotechnology.newrelic.api.NewRelicApi;
import com.ocadotechnology.newrelic.api.model.transactions.KeyTransaction;

import java.util.Optional;

import static java.lang.String.format;

class KeyTransactionIdProvider implements IdProvider {
    @Override
    public Integer getId(NewRelicApi api, String name) {
        Optional<KeyTransaction> ktOptional = api.getKeyTransactionsApi().getByName(name);
        KeyTransaction kt = ktOptional.orElseThrow(
                () -> new NewRelicSyncException(format("Key transaction %s does not exist", name)));
        return kt.getId();
    }
}
