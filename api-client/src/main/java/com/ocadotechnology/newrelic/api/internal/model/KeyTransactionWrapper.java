package com.ocadotechnology.newrelic.api.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.api.model.transactions.KeyTransaction;
import lombok.Value;

@Value
public class KeyTransactionWrapper {
    @JsonProperty("key_transaction")
    KeyTransaction keyTransaction;
}
