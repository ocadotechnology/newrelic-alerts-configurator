package com.ocado.pandateam.newrelic.api.internal;

import com.mashape.unirest.request.HttpRequest;
import com.ocado.pandateam.newrelic.api.KeyTransactionsApi;
import com.ocado.pandateam.newrelic.api.internal.model.KeyTransactionList;
import com.ocado.pandateam.newrelic.api.internal.model.KeyTransactionWrapper;
import com.ocado.pandateam.newrelic.api.model.transactions.KeyTransaction;

import java.util.Optional;

class DefaultKeyTransactionsApi extends ApiBase implements KeyTransactionsApi {

    private static final String KEY_TRANSACTIONS_URL = "/v2/key_transactions.json";
    private static final String KEY_TRANSACTION_URL = "/v2/key_transactions/{key_transaction_id}.json";

    DefaultKeyTransactionsApi(NewRelicRestClient api) {
        super(api);
    }

    @Override
    public Optional<KeyTransaction> getByName(String name) {
        HttpRequest request = api.get(KEY_TRANSACTIONS_URL).queryString("filter[name]", name);
        return api.asObject(request, KeyTransactionList.class).getSingle();
    }

    @Override
    public KeyTransaction getById(int keyTransactionId) {
        HttpRequest request = api.get(KEY_TRANSACTION_URL)
                .routeParam("key_transaction_id", String.valueOf(keyTransactionId));
        return api.asObject(request, KeyTransactionWrapper.class).getKeyTransaction();
    }
}
