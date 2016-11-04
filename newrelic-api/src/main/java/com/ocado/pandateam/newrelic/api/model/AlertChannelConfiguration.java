package com.ocado.pandateam.newrelic.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
@JsonDeserialize(builder = AlertChannelConfiguration.AlertChannelConfigurationBuilder.class)
public class AlertChannelConfiguration {
    @JsonProperty("user_id")
    String userId;
    @JsonProperty
    String channel;
    @JsonProperty
    String url;
    @JsonProperty("include_json_attachment")
    String includeJsonAttachment;
    @JsonProperty
    String recipients;
    @JsonProperty("payload_type")
    String payloadType;
    @JsonProperty
    String payload;
    @JsonProperty
    Map<String, String> headers;
    @JsonProperty("base_url")
    String baseUrl;
    @JsonProperty("auth_username")
    String authUsername;
    @JsonProperty("auth_password")
    String authPassword;
}
