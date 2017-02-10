package com.ocadotechnology.newrelic.api.model.servers;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

/**
 * See <a href="https://rpm.newrelic.com/api/explore/servers/list">Doc</a>
 */
@Value
@Builder
@AllArgsConstructor
public class Server {
    @JsonProperty
    Integer id;
    @JsonProperty("account_id")
    Integer accountId;
    @JsonProperty
    String name;
    @JsonProperty
    String host;
    @JsonProperty
    Boolean reporting;
    @JsonProperty("last_reported_at")
    OffsetDateTime lastReportedAt;
    @JsonProperty
    ServerLinks links;
}
