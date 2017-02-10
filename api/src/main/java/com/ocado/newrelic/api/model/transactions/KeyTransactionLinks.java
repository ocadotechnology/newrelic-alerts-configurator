package com.ocado.newrelic.api.model.transactions;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class KeyTransactionLinks {
    @JsonProperty
    Integer application;
}
