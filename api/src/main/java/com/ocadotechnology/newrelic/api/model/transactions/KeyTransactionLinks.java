package com.ocadotechnology.newrelic.api.model.transactions;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class KeyTransactionLinks {
    @JsonProperty
    Integer application;
}
