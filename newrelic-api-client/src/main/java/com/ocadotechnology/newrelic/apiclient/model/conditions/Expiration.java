package com.ocadotechnology.newrelic.apiclient.model.conditions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Expiration {

    @JsonProperty("expiration_duration")
    String expirationDuration;
    @JsonProperty("open_violation_on_expiration")
    boolean openViolationOnExpiration;
    @JsonProperty("close_violations_on_expiration")
    boolean closeViolationsOnExpiration;

}
