package com.ocadotechnology.newrelic.api.model.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Map;

@Value
@EqualsAndHashCode(exclude = {"url"})
@Builder
@AllArgsConstructor
public class AlertsChannelConfiguration {
    @JsonProperty("user_id")
    Integer userId;
    @JsonProperty
    String channel;
    @JsonProperty
    String url;
    @JsonProperty("include_json_attachment")
    Boolean includeJsonAttachment;
    @JsonProperty
    String recipients;
    @JsonProperty("payload_type")
    String payloadType;
    /**
     * Payload object can be a <code>String</code> or a <code>Map&lt;String, String&gt;</code>.
     */
    @JsonProperty
    Object payload;
    @JsonProperty
    Map<String, String> headers;
    @JsonProperty("base_url")
    String baseUrl;
    @JsonProperty("auth_username")
    String authUsername;
    @JsonProperty("auth_password")
    String authPassword;
    @JsonProperty("service_key")
    String serviceKey;
}
