package com.ocado.pandateam.newrelic.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class AlertChannel {
    @JsonProperty
    Integer id;
    @JsonProperty
    String name;
    @JsonProperty
    String type;
    @JsonProperty
    AlertChannelConfiguration configuration;
    @JsonProperty
    AlertChannelLinks links;

    public static AlertChannel createForUser(String name, String userId) {
        AlertChannelConfiguration config = AlertChannelConfiguration.builder()
                .userId(userId).build();
        return builder().name(name).type("user").configuration(config).build();
    }

    public static AlertChannel createForEmail(String name, String recipients, String includeJsonAttachment) {
        AlertChannelConfiguration config = AlertChannelConfiguration.builder()
                .recipients(recipients).includeJsonAttachment(includeJsonAttachment).build();
        return builder().name(name).type("email").configuration(config).build();
    }

    public static AlertChannel createForSlack(String name, String url, String channel) {
        AlertChannelConfiguration config = AlertChannelConfiguration.builder()
                .url(url).channel(channel).build();
        return builder().name(name).type("slack").configuration(config).build();
    }

}
