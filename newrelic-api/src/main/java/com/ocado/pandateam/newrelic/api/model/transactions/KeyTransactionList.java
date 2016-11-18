package com.ocado.pandateam.newrelic.api.model.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.pandateam.newrelic.api.model.ObjectList;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class KeyTransactionList extends ObjectList<KeyTransaction> {
    @JsonProperty("key_transactions")
    List<KeyTransaction> list;
}
