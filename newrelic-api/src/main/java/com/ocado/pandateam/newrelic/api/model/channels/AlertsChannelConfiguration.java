package com.ocado.pandateam.newrelic.api.model.channels;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class AlertsChannelConfiguration {
    @JsonProperty("user_id")
    String userId;
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

    @java.beans.ConstructorProperties({"userId", "channel", "url", "includeJsonAttachment", "recipients", "payloadType", "payload", "headers", "baseUrl", "authUsername", "authPassword"})
    public AlertsChannelConfiguration(String userId, String channel, String url, Boolean includeJsonAttachment, String recipients, String payloadType, String payload, Map<String, String> headers, String baseUrl, String authUsername, String authPassword) {
        this.userId = userId;
        this.channel = channel;
        this.url = url;
        this.includeJsonAttachment = includeJsonAttachment;
        this.recipients = recipients;
        this.payloadType = payloadType;
        this.payload = payload;
        this.headers = headers;
        this.baseUrl = baseUrl;
        this.authUsername = authUsername;
        this.authPassword = authPassword;
    }

    public static AlertsChannelConfigurationBuilder builder() {
        return new AlertsChannelConfigurationBuilder();
    }

    public String getUserId() {
        return this.userId;
    }

    public String getChannel() {
        return this.channel;
    }

    public String getUrl() {
        return this.url;
    }

    public Boolean getIncludeJsonAttachment() {
        return this.includeJsonAttachment;
    }

    public String getRecipients() {
        return this.recipients;
    }

    public String getPayloadType() {
        return this.payloadType;
    }

    public String getPayload() {
        return this.payload;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public String getAuthUsername() {
        return this.authUsername;
    }

    public String getAuthPassword() {
        return this.authPassword;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof AlertsChannelConfiguration)) return false;
        final AlertsChannelConfiguration other = (AlertsChannelConfiguration) o;
        final Object this$userId = this.userId;
        final Object other$userId = other.userId;
        if (this$userId == null ? other$userId != null : !this$userId.equals(other$userId)) return false;
        final Object this$channel = this.channel;
        final Object other$channel = other.channel;
        if (this$channel == null ? other$channel != null : !this$channel.equals(other$channel)) return false;
        final Object this$url = this.url;
        final Object other$url = other.url;
        if (this$url == null ? other$url != null : !this$url.equals(other$url)) return false;
        final Object this$includeJsonAttachment = this.includeJsonAttachment;
        final Object other$includeJsonAttachment = other.includeJsonAttachment;
        if (this$includeJsonAttachment == null ? other$includeJsonAttachment != null : !this$includeJsonAttachment.equals(other$includeJsonAttachment))
            return false;
        final Object this$recipients = this.recipients;
        final Object other$recipients = other.recipients;
        if (this$recipients == null ? other$recipients != null : !this$recipients.equals(other$recipients)) return false;
        final Object this$payloadType = this.payloadType;
        final Object other$payloadType = other.payloadType;
        if (this$payloadType == null ? other$payloadType != null : !this$payloadType.equals(other$payloadType)) return false;
        final Object this$payload = this.payload;
        final Object other$payload = other.payload;
        if (this$payload == null ? other$payload != null : !this$payload.equals(other$payload)) return false;
        final Object this$headers = this.headers;
        final Object other$headers = other.headers;
        if (this$headers == null ? other$headers != null : !this$headers.equals(other$headers)) return false;
        final Object this$baseUrl = this.baseUrl;
        final Object other$baseUrl = other.baseUrl;
        if (this$baseUrl == null ? other$baseUrl != null : !this$baseUrl.equals(other$baseUrl)) return false;
        final Object this$authUsername = this.authUsername;
        final Object other$authUsername = other.authUsername;
        if (this$authUsername == null ? other$authUsername != null : !this$authUsername.equals(other$authUsername)) return false;
        final Object this$authPassword = this.authPassword;
        final Object other$authPassword = other.authPassword;
        if (this$authPassword == null ? other$authPassword != null : !this$authPassword.equals(other$authPassword)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $userId = this.userId;
        result = result * PRIME + ($userId == null ? 0 : $userId.hashCode());
        final Object $channel = this.channel;
        result = result * PRIME + ($channel == null ? 0 : $channel.hashCode());
        final Object $url = this.url;
        result = result * PRIME + ($url == null ? 0 : $url.hashCode());
        final Object $includeJsonAttachment = this.includeJsonAttachment;
        result = result * PRIME + ($includeJsonAttachment == null ? 0 : $includeJsonAttachment.hashCode());
        final Object $recipients = this.recipients;
        result = result * PRIME + ($recipients == null ? 0 : $recipients.hashCode());
        final Object $payloadType = this.payloadType;
        result = result * PRIME + ($payloadType == null ? 0 : $payloadType.hashCode());
        final Object $payload = this.payload;
        result = result * PRIME + ($payload == null ? 0 : $payload.hashCode());
        final Object $headers = this.headers;
        result = result * PRIME + ($headers == null ? 0 : $headers.hashCode());
        final Object $baseUrl = this.baseUrl;
        result = result * PRIME + ($baseUrl == null ? 0 : $baseUrl.hashCode());
        final Object $authUsername = this.authUsername;
        result = result * PRIME + ($authUsername == null ? 0 : $authUsername.hashCode());
        final Object $authPassword = this.authPassword;
        result = result * PRIME + ($authPassword == null ? 0 : $authPassword.hashCode());
        return result;
    }

    public String toString() {
        return "com.ocado.pandateam.newrelic.api.model.channels.AlertsChannelConfiguration(userId=" + this.userId + ", channel=" + this.channel + ", url=" + this.url + ", includeJsonAttachment=" + this.includeJsonAttachment + ", recipients=" + this.recipients + ", payloadType=" + this.payloadType + ", payload=" + this.payload + ", headers=" + this.headers + ", baseUrl=" + this.baseUrl + ", authUsername=" + this.authUsername + ", authPassword=" + this.authPassword + ")";
    }

    public static class AlertsChannelConfigurationBuilder {
        private String userId;
        private String channel;
        private String url;
        private Boolean includeJsonAttachment;
        private String recipients;
        private String payloadType;
        private String payload;
        private Map<String, String> headers;
        private String baseUrl;
        private String authUsername;
        private String authPassword;

        AlertsChannelConfigurationBuilder() {
        }

        public AlertsChannelConfiguration.AlertsChannelConfigurationBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public AlertsChannelConfiguration.AlertsChannelConfigurationBuilder channel(String channel) {
            this.channel = channel;
            return this;
        }

        public AlertsChannelConfiguration.AlertsChannelConfigurationBuilder url(String url) {
            this.url = url;
            return this;
        }

        public AlertsChannelConfiguration.AlertsChannelConfigurationBuilder includeJsonAttachment(Boolean includeJsonAttachment) {
            this.includeJsonAttachment = includeJsonAttachment;
            return this;
        }

        public AlertsChannelConfiguration.AlertsChannelConfigurationBuilder recipients(String recipients) {
            this.recipients = recipients;
            return this;
        }

        public AlertsChannelConfiguration.AlertsChannelConfigurationBuilder payloadType(String payloadType) {
            this.payloadType = payloadType;
            return this;
        }

        public AlertsChannelConfiguration.AlertsChannelConfigurationBuilder payload(String payload) {
            this.payload = payload;
            return this;
        }

        public AlertsChannelConfiguration.AlertsChannelConfigurationBuilder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public AlertsChannelConfiguration.AlertsChannelConfigurationBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public AlertsChannelConfiguration.AlertsChannelConfigurationBuilder authUsername(String authUsername) {
            this.authUsername = authUsername;
            return this;
        }

        public AlertsChannelConfiguration.AlertsChannelConfigurationBuilder authPassword(String authPassword) {
            this.authPassword = authPassword;
            return this;
        }

        public AlertsChannelConfiguration build() {
            return new AlertsChannelConfiguration(userId, channel, url, includeJsonAttachment, recipients, payloadType, payload, headers, baseUrl, authUsername, authPassword);
        }

        public String toString() {
            return "com.ocado.pandateam.newrelic.api.model.channels.AlertsChannelConfiguration.AlertsChannelConfigurationBuilder(userId=" + this.userId + ", channel=" + this.channel + ", url=" + this.url + ", includeJsonAttachment=" + this.includeJsonAttachment + ", recipients=" + this.recipients + ", payloadType=" + this.payloadType + ", payload=" + this.payload + ", headers=" + this.headers + ", baseUrl=" + this.baseUrl + ", authUsername=" + this.authUsername + ", authPassword=" + this.authPassword + ")";
        }
    }
}
