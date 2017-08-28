package com.ocadotechnology.newrelic.apiclient.internal;

import com.ocadotechnology.newrelic.apiclient.KeyTransactionsApi;
import com.ocadotechnology.newrelic.apiclient.internal.client.NewRelicClient;
import com.ocadotechnology.newrelic.apiclient.internal.model.KeyTransactionList;
import com.ocadotechnology.newrelic.apiclient.internal.model.KeyTransactionWrapper;
import com.ocadotechnology.newrelic.apiclient.model.transactions.KeyTransaction;
import org.glassfish.jersey.uri.UriComponent;

import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.glassfish.jersey.uri.UriComponent.Type.QUERY_PARAM_SPACE_ENCODED;

class DefaultKeyTransactionsApi extends ApiBase implements KeyTransactionsApi {

    private static final String KEY_TRANSACTIONS_URL = "/v2/key_transactions.json";
    private static final String KEY_TRANSACTION_URL = "/v2/key_transactions/{key_transaction_id}.json";

    DefaultKeyTransactionsApi(NewRelicClient client) {
        super(client);
    }

    @Override
    public Optional<KeyTransaction> getByName(String keyTransactionName) {
        String keyTransactionNameEncoded = UriComponent.encode(keyTransactionName, QUERY_PARAM_SPACE_ENCODED);
        return client
                .target(KEY_TRANSACTIONS_URL)
                .queryParam("filter[name]", keyTransactionNameEncoded)
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
