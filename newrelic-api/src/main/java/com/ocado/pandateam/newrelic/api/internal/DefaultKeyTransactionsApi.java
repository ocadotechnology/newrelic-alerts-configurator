package com.ocado.pandateam.newrelic.api.internal;

import com.ocado.pandateam.newrelic.api.KeyTransactionsApi;
import com.ocado.pandateam.newrelic.api.internal.jersey.NewRelicClient;
import com.ocado.pandateam.newrelic.api.internal.model.KeyTransactionList;
import com.ocado.pandateam.newrelic.api.internal.model.KeyTransactionWrapper;
import com.ocado.pandateam.newrelic.api.model.transactions.KeyTransaction;

import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

class DefaultKeyTransactionsApi extends ApiBase implements KeyTransactionsApi {

    private static final String KEY_TRANSACTIONS_URL = "/v2/key_transactions.json";
    private static final String KEY_TRANSACTION_URL = "/v2/key_transactions/{key_transaction_id}.json";

    DefaultKeyTransactionsApi(NewRelicClient client) {
        super(client);
    }

    @Override
    public Optional<KeyTransaction> getByName(String keyTransactionName) {
        return client
                .target(KEY_TRANSACTIONS_URL)
                .queryParam("filter[name]", keyTransactionName)
                .request(APPLICATION_JSON_TYPE)
                .get(KeyTransactionList.class)
                .getSingle();
    }

    @Override
    public KeyTransaction getById(int keyTransactionId) {
        return client
                .target(KEY_TRANSACTION_URL)
                .resolveTemplate("key_transaction_id", keyTransactionId)
                .request(APPLICATION_JSON_TYPE)
                .get(KeyTransactionWrapper.class)
                .getKeyTransaction();
    }
}
