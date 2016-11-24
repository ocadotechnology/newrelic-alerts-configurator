package com.ocado.panda.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocado.panda.newrelic.api.model.transactions.KeyTransaction;
import lombok.Value;

@Value
public class KeyTransactionWrapper {
    @JsonProperty("key_transaction")
    KeyTransaction keyTransaction;
}
