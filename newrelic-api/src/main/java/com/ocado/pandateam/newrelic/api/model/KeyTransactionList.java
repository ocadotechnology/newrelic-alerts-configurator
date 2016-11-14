package com.ocado.pandateam.newrelic.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class KeyTransactionList extends ObjectList<KeyTransaction> {
    @JsonProperty("key_transactions")
    List<KeyTransaction> list;
}
