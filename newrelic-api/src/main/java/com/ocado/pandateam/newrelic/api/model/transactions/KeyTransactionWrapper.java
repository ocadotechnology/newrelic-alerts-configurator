package com.ocado.pandateam.newrelic.api.model.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class KeyTransactionWrapper {
    @JsonProperty("key_transaction")
    KeyTransaction keyTransaction;
}
