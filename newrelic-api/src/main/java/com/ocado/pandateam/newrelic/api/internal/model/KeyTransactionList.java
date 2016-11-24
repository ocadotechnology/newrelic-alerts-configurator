package com.ocado.pandateam.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.pandateam.newrelic.api.model.ObjectList;
import com.ocado.pandateam.newrelic.api.model.transactions.KeyTransaction;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class KeyTransactionList extends ObjectList<KeyTransaction> {
    @JsonProperty("key_transactions")
    List<KeyTransaction> list;

    @Override
    public KeyTransactionList merge(ObjectList<KeyTransaction> list) {
        return new KeyTransactionList(mergeList(list));
    }
}
