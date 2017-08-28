package com.ocadotechnology.newrelic.apiclient.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ocadotechnology.newrelic.apiclient.model.transactions.KeyTransaction;
import lombok.Value;

@Value
public class KeyTransactionWrapper {
    @JsonProperty("key_transaction")
    KeyTransaction keyTransaction;
}
