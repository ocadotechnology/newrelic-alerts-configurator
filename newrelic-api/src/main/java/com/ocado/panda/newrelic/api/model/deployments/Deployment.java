package com.ocado.panda.newrelic.api.model.deployments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

/**
 * See <a href="https://rpm.newrelic.com/api/explore/application_deployments/list">Doc</a>
 */
@Value
@Builder
@AllArgsConstructor
public class Deployment {
    @JsonProperty
    Integer id;
    @JsonProperty
    String revision;
    @JsonProperty
    String changelog;
    @JsonProperty
    String description;
    @JsonProperty
    String user;
    @JsonProperty
    OffsetDateTime timestamp;
    @JsonProperty
    DeploymentLinks links;
}
