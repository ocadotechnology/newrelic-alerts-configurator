package com.ocadotechnology.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.api.model.transactions.KeyTransaction;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class KeyTransactionList extends ObjectList<KeyTransaction, KeyTransactionList> {
    @JsonCreator
    public KeyTransactionList(@JsonProperty("key_transactions") List<KeyTransaction> items) {
        super(items, KeyTransactionList::new);
    }
}
